#ifndef SCHEDULER_SHIPMENT_STATION_H
#define SCHEDULER_SHIPMENT_STATION_H

#include <vector>

#include "boost/date_time/posix_time/posix_time_types.hpp"

#include "coordinate.h"
#include "loadAmmount.h"

namespace Scheduler
{
  struct CShipmentStation
  {
    enum EKind {KLoad, KUnload, KDriveby};

    CShipmentStation()
    : _coord(),
      _kind(KDriveby),
      _timePeriod(boost::posix_time::ptime(boost::gregorian::date(1970,1,1), boost::posix_time::hours(0)),
                  boost::posix_time::ptime(boost::gregorian::date(1970,1,1), boost::posix_time::hours(0))),
      _cargo()
    {}

    CCoordinate                    _coord;

    EKind                          _kind;
    boost::posix_time::time_period _timePeriod;
    std::vector<CCargo>            _cargo;
  };
}

#endif // SCHEDULER_SHIPMENT_STATION_H