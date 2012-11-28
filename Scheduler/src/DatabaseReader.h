#ifndef SCHEDULER_DATABASE_READER_H
#define SCHEDULER_DATABASE_READER_H

#include <time.h>
#include "boost/date_time/posix_time/posix_time_types.hpp"
#include <stdexcept>

#include "DatabaseConnectible.h"
#include "situation.h"

namespace Scheduler
{
  class CDatabaseReader : public CDatabaseConnectible
  {
    public:
      static const unsigned int MinimumMinutesBeforeContractDecay = 15;

      //! @param aConnectible a structure containing credentials
      CDatabaseReader(const CDatabaseConnectible& aConnectible);

      void DoReadFromDatabase(CFramingData& framingData);

    protected:
      void ReadContracts(std::vector<CContract>& contracts);
      void ReadContractStations(std::vector<CContract>& contracts);

      //! reads trucks table and already applies filtering on vehicle capacity according to contracts
      void ReadTrucks(std::vector<CTruck>& trucks, const std::vector<CContract>& contracts);

      void ReadTruckRoutes(CFramingData& framingData);

    private:
      //! assumes, the row contains columns "latitude" and "longitude" in format DECIMAL(8,2)
      void ReadCoordinate(CCoordinate& coord, const sql::ResultSet& rs);
      CShipmentStation::EKind ReadShipmentStationKind(const sql::ResultSet& rs);
      void ReadTimePeriod(boost::posix_time::time_period& aTimePeriod, const sql::ResultSet& rs);

      void ReadContractRow(CContract& contract, const sql::ResultSet& rs);
      void ReadContractStationsRow(CShipmentStation& station, const sql::ResultSet& rs);

      void ReadTruckRow(CTruck& aTruck, const sql::ResultSet& rs);
      void ReadRouteStationRow(CRouteStation& routeStation, const sql::ResultSet& rs);

      void ReadCargoRow(CCargo& cargo, const sql::ResultSet& rs);

      void CheckTimePeriodsForItinerary(const TItinerary& aItinerary);

    public:
      //! special exception class for Database reader
      struct CDBReaderException : public std::runtime_error
      {
        CDBReaderException(const std::string &err) : runtime_error (err ) {}
      };

  };
}

#endif // SCHEDULER_DATABASE_READER_H