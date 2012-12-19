#include <string>
#include <stdlib.h>
#include <iostream>
#include <stdexcept>

#include <UnitTest++.h>

#include "boost/date_time/posix_time/posix_time.hpp" //include all time types plus i/o

#include "../xmlHelper.h"

namespace Scheduler{
namespace Test{

  struct CXmlTestFixture : public CXmlHelper
  {
    CXmlTestFixture() : CXmlHelper()
    {
    }

    ~CXmlTestFixture()
    {
      // tear down
    }
  };


  TEST_FIXTURE(CXmlTestFixture, TestReadLoadingKind)
  {
    std::string xmlStrLoad = "<kind>LOAD</kind>";
    std::string xmlStrUnLoad = "<kind>UNLOAD</kind>";
    std::string xmlStrDriveBy = "<kind>DRIVE_BY</kind>";
    std::string xmlStrErr = "<kind>driveby</kind>";

    _currentDoc.Clear();
    LoadXml(xmlStrLoad);
    CShipmentStation::EKind kindLoad;
    bool loadParsed = ParseStationLoadingKind(_currentDoc.RootElement(), kindLoad);
    CHECK(loadParsed);
    CHECK_EQUAL(CShipmentStation::KLoad, kindLoad);

    _currentDoc.Clear();
    LoadXml(xmlStrUnLoad);
    CShipmentStation::EKind kindUnLoad;
    bool unloadParsed = ParseStationLoadingKind(_currentDoc.RootElement(), kindUnLoad);
    CHECK(unloadParsed);
    CHECK_EQUAL(CShipmentStation::KUnload, kindUnLoad);

    _currentDoc.Clear();
    LoadXml(xmlStrDriveBy);
    CShipmentStation::EKind kindDriveBy;
    bool driveByParsed = ParseStationLoadingKind(_currentDoc.RootElement(), kindDriveBy);
    CHECK(driveByParsed);
    CHECK_EQUAL(CShipmentStation::KDriveby, kindDriveBy);

    _currentDoc.Clear();
    LoadXml(xmlStrErr);
    CShipmentStation::EKind kindErr;
    bool errParsed = ParseStationLoadingKind(_currentDoc.RootElement(), kindErr);
    CHECK(!errParsed);
  }

  TEST_FIXTURE(CXmlTestFixture, TestReadLatLong)
  {
    CCoordinate coord;

    std::string xml1 = "<station><latitude>52.12345</latitude><longitude>2.9875</longitude></station>";
    _currentDoc.Clear();
    LoadXml(xml1);
    bool parsed1 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(parsed1);
    CHECK_EQUAL(52123450, coord._lat);
    CHECK_EQUAL( 2987500, coord._long);

    std::string xml2 = "<station><latitude>0.0</latitude><longitude>-1</longitude></station>";
    _currentDoc.Clear();
    LoadXml(xml2);
    bool parsed2 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(parsed2);
    CHECK_EQUAL(0, coord._lat);
    CHECK_EQUAL(-1000000, coord._long);

    std::string xml3 = "<station><latitude>0</latitude><longitude>1.00</longitude></station>";
    _currentDoc.Clear();
    LoadXml(xml3);
    bool parsed3 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(parsed3);
    CHECK_EQUAL(0, coord._lat);
    CHECK_EQUAL(1000000, coord._long);

    std::string xml4 = "<station><longitude>0</longitude><latitude>1.00</latitude></station>";
    _currentDoc.Clear();
    LoadXml(xml4);
    bool parsed4 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(parsed4);
    CHECK_EQUAL(1000000, coord._lat);
    CHECK_EQUAL(0, coord._long);

    std::string xml5 = "<station><longitude>180.5</longitude><latitude>1.00</latitude></station>";
    _currentDoc.Clear();
    LoadXml(xml5);
    bool parsed5 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(!parsed5); // parsing not successful, becas longitude between -180 and 180

    std::string xml6 = "<station><longitude>19.5</longitude><latitude>90.02</latitude></station>";
    _currentDoc.Clear();
    LoadXml(xml6);
    bool parsed6 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(!parsed6); // parsing not successful, becaus latitude between -90 and 90

    std::string xml7 = "<station><latitude>-82.3</latitude><longitude>180</longitude></station>";
    _currentDoc.Clear();
    LoadXml(xml7);
    bool parsed7 = ParseLatLong(_currentDoc.RootElement(), coord);
    CHECK(parsed7);
    CHECK_EQUAL(-82300000, coord._lat);
    CHECK_EQUAL(180000000, coord._long);
  }


  TEST_FIXTURE(CXmlTestFixture, TestReadContract)
  {
  }


}} // namespaces