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

  TEST_FIXTURE(CXmlTestFixture, TestReadContract)
  {
  }


}} // namespaces