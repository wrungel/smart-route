#ifndef SCHEDULER_SHIPMENT_STATION_H
#define SCHEDULER_SHIPMENT_STATION_H

#include <vector>

#include "boost/date_time/posix_time/posix_time_types.hpp"

#include "coordinate.h"
#include "loadAmmount.h"

namespace Scheduler
{
  static inline boost::posix_time::ptime DefaultDateTime()
  {
    return  boost::posix_time::ptime(boost::gregorian::date(1970,1,1), boost::posix_time::hours(0));
  }

  static inline boost::posix_time::time_period DefaultTimePeriod()
  {
    return  boost::posix_time::time_period(DefaultDateTime(), DefaultDateTime());
  }

  /*! represents stations in contract or / and in routes.
  * If in route, it still can have some information about the contract as the contract itself may be not loaded from database
  */
  struct CShipmentStation
  {
    enum EKind {KLoad, KUnload, KDriveby};

    CShipmentStation()
    : _coord(),
      _kind(KDriveby),
      _timePeriod(DefaultTimePeriod()),
      _cargo()
    {}

    CCoordinate                    _coord;

    EKind                          _kind;
    //! period specified in contract
    boost::posix_time::time_period _timePeriod;
    //! to load/unload as specified in contract
    std::vector<CCargo>            _cargo;
  };
}

#endif // SCHEDULER_SHIPMENT_STATION_H