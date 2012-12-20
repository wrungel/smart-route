#include "xmlHelper.h"

#include "HelperFunctions.h"
#include "shipmentStation.h"

namespace Scheduler
{
  bool CXmlHelper::ParseStationLoadingKind(const TiXmlElement* xmlElement, CShipmentStation::EKind& eKind)
  {
    eKind = CShipmentStation::KDriveby;

    const char* cstr = xmlElement->GetText();
    if (!cstr)
    {
      return false;
    }
    std::string str(cstr);
    str = trim(str);

    if (str == "LOAD")
    {
      eKind = CShipmentStation::KLoad;
      return true;
    }
    else if (str == "UNLOAD")
    {
      eKind = CShipmentStation::KUnload;
      return true;
    }
    else if (str == "DRIVE_BY")
    {
      eKind = CShipmentStation::KDriveby;
      return true;
    }

    return false;
  }

  bool CXmlHelper::ParseLatLong(const TiXmlElement* fatherElement, CCoordinate& coordinate)
  {
    //"latitude""longitude"
    const TiXmlElement* latitudeElem = fatherElement->FirstChildElement("latitude");
    const TiXmlElement* longitudeElem = fatherElement->FirstChildElement("longitude");
    if (!latitudeElem || !longitudeElem)
    {
      return false;
    }

    std::string latStr = latitudeElem->GetText();
    trim(latStr);
    coordinate._lat = ParseDecimal(latStr, CCoordinate::KDigitsAfterComma);
    std::string longStr = longitudeElem->GetText();
    trim(longStr);
    coordinate._long = ParseDecimal(longStr, CCoordinate::KDigitsAfterComma);

    bool parsingSuccessful = coordinate.IsValid();
    return parsingSuccessful;
  }

  boost::posix_time::ptime CXmlHelper::ParseDateTime(const TiXmlElement* xmlElement)
  {
      std::string str = xmlElement->GetText();
      trim(str);
      size_t delimiterPosition = str.find('T');
      if (delimiterPosition != std::string::npos)
      {
        str.replace(delimiterPosition, 1, " ");
      }
      return boost::posix_time::time_from_string(str);
  }

  bool CXmlHelper::ParseTimePeriod(const TiXmlElement* fatherElement, boost::posix_time::time_period& timePeriod)
  {
    try{
      const TiXmlElement* timeFromElement = fatherElement->FirstChildElement("timeFrom");
      boost::posix_time::ptime timeFrom = ParseDateTime(timeFromElement);

      const TiXmlElement* timeUntilElement = fatherElement->FirstChildElement("timeUntil");
      boost::posix_time::ptime timeUntil = ParseDateTime(timeUntilElement);

      timePeriod = boost::posix_time::time_period(timeFrom, timeUntil);
      return timeFrom <= timeUntil;
    }
    catch (...)
    {
      return false;
    }
  }

  bool CXmlHelper::ParseStation(const TiXmlElement* stationElement, CShipmentStation& station)
  {
    bool success = true;

    // coordinates parsing
    success &= ParseLatLong(stationElement, station._coord);

    // parse: load/unload/driveby
    const TiXmlElement* loadingKindElement = stationElement->FirstChildElement("kind");
    success &= loadingKindElement && ParseStationLoadingKind(loadingKindElement, station._kind);

    success &= ParseTimePeriod(stationElement, station._timePeriod);

    // load ammount parsing
    const TiXmlElement* unitElement = stationElement->FirstChildElement("units");
    int units = atoi(unitElement->GetText());
    station._cargo.push_back(CCargo(0, 0, static_cast<unsigned short>(units), "unknown"));
    success &= units > 0;

    return success;
  }

  std::auto_ptr<CContract> CXmlHelper::ReadContract(const std::string& xmlString)
  {
    _currentDoc.Clear();
    LoadXml(xmlString);

    std::auto_ptr<CContract> contract(new CContract);
    _currentDoc.RootElement()->QueryBoolAttribute("sealed", &(contract->_sealed));

    const TiXmlElement* stationElement = _currentDoc.RootElement()->FirstChildElement("station");
    while (stationElement)
    {
      boost::shared_ptr<CShipmentStation> station(new CShipmentStation);
      bool stationParsedSuccessfully = ParseStation(stationElement, *station);
      if (stationParsedSuccessfully)
      {
        contract->_stationSequence.push_back(station);
      }

      stationElement = stationElement->NextSiblingElement("station");
    }
    return contract;
  }
}