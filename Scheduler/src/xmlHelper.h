#ifndef SCHEDULER_XML_HELPER_H
#define SCHEDULER_XML_HELPER_H

#include <iostream>
#include <string>

#include <auto_ptr.h>

#include "boost/date_time/posix_time/posix_time.hpp" //include all time types plus i/o

#define TIXML_USE_STL
#include "tinyxml.h"

#include "contract.h"

namespace Scheduler
{

  class CXmlHelper
  {
  public:
    std::auto_ptr<CContract> ReadContract(const std::string& xmlString);

  protected: // most following methods are accessed by unit tests through inhreitance

    void LoadXml(const std::string& xmlStr) { _currentDoc.Parse(xmlStr.c_str(), 0, TIXML_ENCODING_UTF8); }

    bool ParseStationLoadingKind(const TiXmlElement* xmlElement, CShipmentStation::EKind& eKind);
    bool ParseLatLong(const TiXmlElement* fatherElement, CCoordinate& coordinate);
    bool ParseTimePeriod(const TiXmlElement* fatherElement, boost::posix_time::time_period& timePeriod);
    boost::posix_time::ptime ParseDateTime(const TiXmlElement* xmlElement);

    TiXmlDocument _currentDoc;

  private:
    bool ParseStation(const TiXmlElement* xmlElement, CShipmentStation& station);
  };
}

#endif // SCHEDULER_XML_HELPER_H