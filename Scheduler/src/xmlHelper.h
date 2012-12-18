#ifndef SCHEDULER_XML_HELPER_H
#define SCHEDULER_XML_HELPER_H

#include <iostream>
#include <string>

#include <auto_ptr.h>

#define TIXML_USE_STL
#include "tinyxml.h"

#include "contract.h"

namespace Scheduler
{
  namespace Test
  {
    class CXmlTestFixture;
  }

  class CXmlHelper
  {
  public:
    std::auto_ptr<CContract> ReadContract(const std::string& xmlString);

  protected:
    void LoadXml(const std::string& xmlStr) { _currentDoc.Parse(xmlStr.c_str(), 0, TIXML_ENCODING_UTF8); }

    bool ParseStationLoadingKind(const TiXmlElement* xmlElement, CShipmentStation::EKind& eKind);

    TiXmlDocument _currentDoc;

    friend class Test::CXmlTestFixture;
  };
}

#endif // SCHEDULER_XML_HELPER_H