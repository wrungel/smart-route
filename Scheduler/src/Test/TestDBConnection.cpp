#include <string>
#include <stdlib.h>
#include <iostream>
#include <stdexcept>

#include <UnitTest++.h>

#include <memory>

#include "boost/date_time/posix_time/posix_time.hpp" //include all time types plus i/o

#include "../mySqlHelper.h"

#include "../DatabaseReader.h"

namespace Scheduler{
namespace Test{

  struct DatabaseTestFixture
  {
    DatabaseTestFixture()
      : _connectible( // setup
        /* _host = */"localhost",
        /* _user = */"TestScheduler",
        /* _password = */"TestScheduler5",
        /* _database = */"LkwSchedulerDBTest"),

        _dbReader(_connectible)
    {
      DoMySqlCall("reset_tables.sql");

      try
      {
        _connectible.Open();
      }
      catch (sql::SQLException& e)
      {
        PrintSqlException(e);
        throw e;
      }

      try
      {
        _dbReader.Open();
      }
      catch (sql::SQLException& e)
      {
        PrintSqlException(e);
        throw e;
      }
    }

    ~DatabaseTestFixture()
    {
      _dbReader.Close();
      // tear down
    }

  protected:
    CDatabaseConnectible _connectible;

    sql::Connection* Connection() { return _connectible.Connection(); }

    void DoMySqlCall(const std::string& aSqlScript)
    {
      std::string callStr = "mysql --user=" + _connectible.User() + " --password=" + _connectible.Password() + " " + _connectible.Database() + " < " + aSqlScript;
      system(callStr.c_str());
    }

  protected:
    class CTestedDatabaseReader : public CDatabaseReader
    {
      public:
        CTestedDatabaseReader(const CDatabaseConnectible& aConnectible)
        : CDatabaseReader(aConnectible)
        {}

        void ReadContracts(std::vector<CContract>& contracts) { CDatabaseReader::ReadContracts(contracts); }
        void ReadContractStations(std::vector<CContract>& contracts) { CDatabaseReader::ReadContractStations(contracts); }
        void ReadTrucks(std::vector<CTruck>& trucks, const std::vector<CContract>& contracts)
         { CDatabaseReader::ReadTrucks(trucks, contracts); }
        void ReadTruckRoutes(CFramingData& framingData)  { CDatabaseReader::ReadTruckRoutes(framingData); }
    };

    CTestedDatabaseReader _dbReader;
  };


  TEST_FIXTURE(DatabaseTestFixture, TestReadCustomer)
  {
    try
    {
      std::string insertAccountStr = "INSERT INTO Account SET login='testtestcustomer', passwd='123';";
      std::auto_ptr<sql::Statement> insertAccountStmt(Connection()->createStatement());
      insertAccountStmt->execute(insertAccountStr);
      int accountId = getLastInsertId(Connection());

      std::stringstream insertStrStream;
      insertStrStream << "INSERT INTO Customer SET account_id=" << accountId << ", address='Elsenstr. 105, 12435 Berlin', phone='0123-1234567', companyName='TestCompany';";
      std::string insertStr = insertStrStream.str();
      std::auto_ptr<sql::Statement> insertStmt(Connection()->createStatement());
      insertStmt->execute(insertStr);

      int id = getLastInsertId(Connection());
      CHECK(id != 0);

      std::auto_ptr<sql::PreparedStatement> selectStmt(Connection()->prepareStatement("SELECT * FROM Customer WHERE id=?;"));
      selectStmt->setInt(1, id);

      std::auto_ptr<sql::ResultSet> rs1(selectStmt->executeQuery());
      CHECK_EQUAL(1, rs1->rowsCount());

      std::auto_ptr<sql::PreparedStatement> delStmt(Connection()->prepareStatement("DELETE FROM Customer WHERE id=?;"));
      delStmt->setInt(1, id);
      delStmt->executeUpdate();

      std::auto_ptr<sql::ResultSet> rs2(selectStmt->executeQuery());
      CHECK_EQUAL(0, rs2->rowsCount());
    }
    catch(sql::SQLException &e)
    {
      PrintSqlException(e);
      throw(e);
    }
  }

  TEST_FIXTURE(DatabaseTestFixture, TestReadDB1)
  {
    DoMySqlCall("insert_contracts1.sql");
    DoMySqlCall("insert_trucks1.sql");

    CFramingData frameData;

    _dbReader.ReadContracts(frameData._contracts);
    CHECK(frameData._contracts.size() > 0);

    CContract& firstContract = frameData._contracts[0];
    CHECK_EQUAL(true, firstContract._sealed);
    CHECK_EQUAL(99999, firstContract._price);
    CHECK_EQUAL(1000, firstContract._loadAmmount._weightKg);
    CHECK_EQUAL(3200,firstContract._loadAmmount._liter);
    CHECK_EQUAL(5, firstContract._loadAmmount._units);

    _dbReader.ReadContractStations(frameData._contracts);
    CHECK(firstContract._stationSeqence.size() == 2);
    boost::shared_ptr<CShipmentStation> dep = firstContract._stationSeqence[0];
    boost::shared_ptr<CShipmentStation> arr = firstContract._stationSeqence[1];

    CHECK_EQUAL(52489653, dep->_coord._lat);
    CHECK_EQUAL(13454837, dep->_coord._long);
    CHECK_EQUAL(51241622, arr->_coord._lat);
    CHECK_EQUAL( 6852770, arr->_coord._long);

    CHECK_EQUAL(CShipmentStation::KLoad, dep->_kind);
    CHECK_EQUAL( CShipmentStation::KUnload, arr->_kind);

    CHECK_EQUAL(2, dep->_cargo.size());
    CHECK_EQUAL(2, arr->_cargo.size());
    for (int i = 0; i < 2; i++)
    {
      CHECK(dep->_cargo[i]._cargoType != "");
      CHECK(arr->_cargo[i]._cargoType != "");
    }
    CHECK(dep->_cargo[0]._cargoType != dep->_cargo[1]._cargoType);
    CHECK(arr->_cargo[0]._cargoType != arr->_cargo[1]._cargoType);

    CHECK_EQUAL(1000, dep->_cargo[0]._weightKg + dep->_cargo[1]._weightKg);
    CHECK_EQUAL(3200, dep->_cargo[0]._liter + dep->_cargo[1]._liter);
    CHECK_EQUAL(5, dep->_cargo[0]._units + dep->_cargo[1]._units);
    CHECK_EQUAL(1000, arr->_cargo[0]._weightKg + arr->_cargo[1]._weightKg);
    CHECK_EQUAL(3200, arr->_cargo[0]._liter + arr->_cargo[1]._liter);
    CHECK_EQUAL(5, arr->_cargo[0]._units + arr->_cargo[1]._units);
    {
      using namespace boost::posix_time;
      using namespace boost::gregorian;
      ptime depStart(date(2013, May, 2), hours(12));
      ptime depEnd(date(2013, May, 2), hours(18));
      ptime arrStart(date(2013, May, 3), hours(12));
      ptime arrEnd(date(2013, May, 3), hours(18));
      CHECK(depStart == dep->_timePeriod.begin());
      CHECK(depEnd == dep->_timePeriod.end());
      CHECK(arrStart == arr->_timePeriod.begin());
      CHECK(arrEnd == arr->_timePeriod.end());
    }

    _dbReader.ReadTrucks(frameData._trucks, frameData._contracts);

    CHECK_EQUAL(2, frameData._trucks.size());

    CHECK_EQUAL(5, frameData._trucks[0]._capacity._units);
    CHECK_EQUAL(3000, frameData._trucks[0]._capacity._weightKg);
    CHECK_EQUAL(15200, frameData._trucks[0]._capacity._liter);
    CHECK_EQUAL(110, frameData._trucks[0]._priceKm);
    CHECK_EQUAL(850, frameData._trucks[0]._priceHour);
    CHECK_EQUAL(51241622, frameData._trucks[0]._homeStation._lat);
    CHECK_EQUAL(6852770, frameData._trucks[0]._homeStation._long);

    CHECK_EQUAL(5, frameData._trucks[1]._capacity._units);
    CHECK_EQUAL(2000, frameData._trucks[1]._capacity._weightKg);
    CHECK_EQUAL(11200, frameData._trucks[1]._capacity._liter);
    CHECK_EQUAL(95, frameData._trucks[1]._priceKm);
    CHECK_EQUAL(650, frameData._trucks[1]._priceHour);
    CHECK_EQUAL(52489653, frameData._trucks[1]._homeStation._lat);
    CHECK_EQUAL(13454837, frameData._trucks[1]._homeStation._long);

    _dbReader.ReadTruckRoutes(frameData);

    CHECK_EQUAL(2, frameData._truckRoutes.size());
    CHECK_EQUAL(0, frameData._truckRoutes[0].size());
    CHECK_EQUAL(2, frameData._truckRoutes[1].size());
  }



}} // namespaces