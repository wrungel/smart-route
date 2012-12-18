#include "xmlHelper.h"

#include "shipmentStation.h"

namespace Scheduler
{
  std::auto_ptr<CContract> CXmlHelper::ReadContract(const std::string& xmlString)
  {
    // TODO
    return std::auto_ptr<CContract>(new CContract);
  }

  bool CXmlHelper::ParseStationLoadingKind(const TiXmlElement* xmlElement, CShipmentStation::EKind& eKind)
  {
    eKind = CShipmentStation::KDriveby;

    const char* cstr = xmlElement->GetText();
    if (!cstr)
    {
      return false;
    }

    if (strncmp(cstr, "LOAD", 20) == 0)
    {
      eKind = CShipmentStation::KLoad;
      return true;
    }
    else if (strncmp(cstr, "UNLOAD", 20) == 0)
    {
      eKind = CShipmentStation::KUnload;
      return true;
    }
    else if (strncmp(cstr, "DRIVE_BY", 20) == 0)
    {
      eKind = CShipmentStation::KDriveby;
      return true;
    }

    return false;
  }

}