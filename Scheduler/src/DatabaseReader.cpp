#include "DatabaseReader.h"

#include "boost/date_time/posix_time/posix_time.hpp" //include all time types plus i/o

#include <boost/assert.hpp>
#include <boost/algorithm/string.hpp>
#include <sstream>
#include <algorithm>

#include "HelperFunctions.h"
#include "mySqlHelper.h"

using namespace Scheduler;

CDatabaseReader::CDatabaseReader(const CDatabaseConnectible& aConnectible)
: CDatabaseConnectible(aConnectible.Host(),
                       aConnectible.User(),
                       aConnectible.Password(),
                       aConnectible.Database())
{
}


void CDatabaseReader::DoReadFromDatabase(CFramingData& framingData)
{
  ReadContracts(framingData._contracts);
  ReadContractStations(framingData._contracts);
  ReadTrucks(framingData._trucks, framingData._contracts);
  framingData._truckRoute.reserve(framingData._trucks.size());
  ReadTruckRoutes(framingData);
}

void CDatabaseReader::ReadContracts(std::vector<CContract>& contracts)
{
  BOOST_ASSERT(contracts.size() == 0);

  boost::posix_time::ptime now(boost::posix_time::second_clock::local_time());
  boost::posix_time::ptime earliestDecay = now + boost::posix_time::minutes(MinimumMinutesBeforeContractDecay);


  static std::string readContractsSql = "SELECT * FROM Contract"
   " WHERE ((decayTime > FROM_UNIXTIME(?)) AND (assignmentId IS NULL) AND (toBeAssigned = true));";

  std::auto_ptr<sql::PreparedStatement> readContractsStmt(_connection->prepareStatement(readContractsSql));
  readContractsStmt->setUInt(1, To_time_t(earliestDecay));

  std::auto_ptr<sql::ResultSet> contractsRS(readContractsStmt->executeQuery());
  while(contractsRS->next())
  {
    CContract contract;
    ReadContractRow(contract, *contractsRS);
    contracts.push_back(contract);
  }
}

void CDatabaseReader::ReadContractRow(CContract& aContract, const sql::ResultSet& rs)
{
   aContract._id = rs.getInt("id");
   //  _wasSuggestedBefore;             // some assignemnt for this contract was already suggested by an earliear run
   aContract._sealed = rs.getBoolean("sealed");
   aContract._price = getDecimal(rs, "price", 2); // cents
   aContract._loadAmmount._weightKg = rs.getUInt("entireWeightKg");
   aContract._loadAmmount._liter = getDecimal(rs, "entireVolumeM3", 3);       // volume
   aContract._loadAmmount._units = rs.getUInt("entireVolumeUnits"); // paletten;
}

void CDatabaseReader::ReadContractStations(std::vector<CContract>& contracts)
{
  BOOST_ASSERT(contracts.size() > 0);

  static std::string readContractStationsSql =
    "SELECT * FROM ContractStation WHERE contract_id=? ORDER BY numberInSequence;";

  std::auto_ptr<sql::PreparedStatement> readContractStationStmt(_connection->prepareStatement(readContractStationsSql));

  for(std::vector<CContract>::iterator contract = contracts.begin(); contract != contracts.end(); ++contract)
  {
    BOOST_ASSERT(contract->_itinerary.size() == 0);
    try
    {
      readContractStationStmt->setInt(1, contract->_id);
      std::auto_ptr<sql::ResultSet> contractStationsRS(readContractStationStmt->executeQuery());

      while(contractStationsRS->next())
      {
        boost::shared_ptr<CShipmentStation> station(new CShipmentStation);
        ReadContractStationsRow(*station, *contractStationsRS);
        contract->_itinerary.push_back(station);
      }

      CheckTimePeriodsForItinerary(contract->_itinerary);
    }
    catch (CDBReaderException& err)
    {
      // TODO 1) log
      // TODO 2) remove erroneous contract
    }
  }
}

void CDatabaseReader::ReadContractStationsRow(CShipmentStation& station, const sql::ResultSet& rs)
{
  ReadCoordinate(station._coord, rs);
  station._kind = ReadShipmentStationKind(rs);
  //station._loadAmmount._weightKg = rs.getInt("weightKg");
  //station._loadAmmount._liter = getDecimal(rs, "volumeM3", 3);       // volume
  //station._loadAmmount._units = rs.getUInt("units"); // paletten;
  ReadTimePeriod(station._timePeriod, rs);
}

CShipmentStation::EKind CDatabaseReader::ReadShipmentStationKind(const sql::ResultSet& rs)
{
  std::string kindStr = rs.getString("kind");
  boost::algorithm::to_lower(kindStr);
  if (kindStr == "load")
  {
    return CShipmentStation::KLoad;
  }
  else if (kindStr == "unload")
  {
    return CShipmentStation::KUnload;
  }
  else if (kindStr == "driveby")
  {
    return CShipmentStation::KDriveby;
  }
  else throw new CDBReaderException("Unknown kind of Shipment Station '" + kindStr + "' ??");
}

void CDatabaseReader::ReadCoordinate(CCoordinate& coord, const sql::ResultSet& rs)
{
  coord._lat = getDecimal(rs, "latitude", 6);
  coord._long = getDecimal(rs, "longitude", 6);
}

void CDatabaseReader::ReadTimePeriod(boost::posix_time::time_period& aTimePeriod,
                                     const sql::ResultSet& rs)
{
  using namespace boost::posix_time;
  using namespace boost::gregorian;

  std::string startStr = rs.getString("timeFrom");
  std::string endStr = rs.getString("timeUntil");
  ptime timeFrom = time_from_string(startStr);
  ptime timeUntil = time_from_string(endStr);

  if (timeFrom > timeUntil)
  {
    throw new CDBReaderException("reading bad time interval: timeFrom (" + startStr + ") is behind the timeUntil (" + endStr + ")");
  }

  aTimePeriod = time_period(timeFrom, timeUntil);
}

void CDatabaseReader::CheckTimePeriodsForItinerary(const TItinerary& aItinerary)
{
  for (unsigned char i = 0; i < aItinerary.size(); ++i)
  {
    for (unsigned char j = i + 1; j < aItinerary.size(); ++j)
    {
      if (aItinerary[i]->_timePeriod.is_after(aItinerary[j]->_timePeriod.end()))
      {
        std::stringstream exceptionText;
        exceptionText << "time period for station " << i << " is after tme period for station " << j;
        throw CDBReaderException(exceptionText.str());
      }
    }
  }
}

void CDatabaseReader::ReadTrucks(std::vector<CTruck>& trucks,
                                 const std::vector<CContract>& contracts)
{
  BOOST_ASSERT(trucks.size() == 0);
  BOOST_ASSERT(contracts.size() > 0);

  CLoadAmmount preliminaryConstraint;
  preliminaryConstraint.SetMaximumValues();
  for(std::vector<CContract>::const_iterator it = contracts.begin(); it != contracts.end(); ++it)
  {
    preliminaryConstraint = GetMin(preliminaryConstraint, it->_loadAmmount);
  }

  static std::string readTrucksSql =
  "SELECT id, capacityM3, capacityUnits, capacityKg, priceKm, priceHour, latHome, longHome"
  " FROM Truck WHERE (isAvailable=true"
  " AND capacityM3 >= (?) AND capacityUnits >= (?) AND capacityKg >= (?));";

  std::auto_ptr<sql::PreparedStatement> readTrucksStmt(_connection->prepareStatement(readTrucksSql));
  setDecimal(*readTrucksStmt, 1, preliminaryConstraint._liter, 3);
  readTrucksStmt->setInt(2, preliminaryConstraint._units);
  readTrucksStmt->setInt(3, preliminaryConstraint._weightKg);

  std::auto_ptr<sql::ResultSet> trucksRS(readTrucksStmt->executeQuery());
  while(trucksRS->next())
  {
    CTruck truck;
    ReadTruckRow(truck, *trucksRS);
    trucks.push_back(truck);
  }
}

void CDatabaseReader::ReadTruckRow(CTruck& aTruck, const sql::ResultSet& rs)
{
   aTruck._id = rs.getInt("id");
   aTruck._capacity._weightKg = rs.getUInt("capacityKg");
   aTruck._capacity._liter = getDecimal(rs, "capacityM3", 3);    // volume
   aTruck._capacity._units = rs.getUInt("capacityUnits");        // paletten;

   aTruck._homeStation._lat = getDecimal(rs, "latHome", 6);
   aTruck._homeStation._long = getDecimal(rs, "longHome", 6);

   aTruck._priceHour = getDecimal(rs, "priceHour", 2);
   aTruck._priceKm = getDecimal(rs, "priceKm", 2);
}

void CDatabaseReader::ReadTruckRoutes(CFramingData& framingData)
{
  static std::string readRouteStationsSql =
  "SELECT *"
  " FROM RouteStation JOIN Route WHERE Route.id=RouteStation.route_id"
  " AND Route.truck_id=(?) AND Route.isRealRoute=true"
  " GROUP BY RouteStation.numberInSequence";

  std::auto_ptr<sql::PreparedStatement> readRouteStationsStmt(_connection->prepareStatement(readRouteStationsSql));

  for(TTruckIndex truckIndex = 0; truckIndex < static_cast<TTruckIndex>(framingData._trucks.size()); ++truckIndex)
  {
    const CTruck& truck = framingData._trucks[truckIndex];
    readRouteStationsStmt->setInt(1, truck._id);
    std::auto_ptr<sql::ResultSet> routeStationsRS(readRouteStationsStmt->executeQuery());
    TRoute route;
    while(routeStationsRS->next())
    {
      CRouteStation routeStation;
      ReadRouteStationRow(routeStation, *routeStationsRS);
      route.push_back(routeStation);
    }
    framingData._truckRoute.push_back(route);
  }
}

void CDatabaseReader::ReadRouteStationRow(CRouteStation& routeStation, const sql::ResultSet& rs)
{
  boost::shared_ptr<CShipmentStation> shipmenStation(new CShipmentStation);
  ReadContractStationsRow(*shipmenStation, rs);
  routeStation._station = shipmenStation;
  routeStation._capacity._weightKg = rs.getUInt("leftKg");
  routeStation._capacity._liter = getDecimal(rs, "leftM3", 3);  // volume
  routeStation._capacity._units = rs.getUInt("leftUnits");
  if (!rs.isNull("contract_id"))
  {
    routeStation._contractIndex = CRouteStation::KUnloadedContract;
  }
  else
  {
    routeStation._contractIndex = CRouteStation::KNoContract;
  }
}