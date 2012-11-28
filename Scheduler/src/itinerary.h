#ifndef SCHEDULER_ITINERARY_H
#define SCHEDULER_ITINERARY_H

#include <vector>

#include "boost/shared_ptr.hpp"
#include "boost/weak_ptr.hpp"
#include "boost/date_time/posix_time/posix_time_types.hpp"

#include "loadAmmount.h"

namespace Scheduler
{
  struct CCoordinate
  {
    //! latitude * 10^6
    int _lat;
    //! longitude * 10^6
    int _long;
  };

  struct CShipmentStation
  {
    CShipmentStation()
    : _coord(),
      _kind(KDriveby),
      _timePeriod(boost::posix_time::ptime(boost::gregorian::date(1970,1,1), boost::posix_time::hours(0)),
                  boost::posix_time::ptime(boost::gregorian::date(1970,1,1), boost::posix_time::hours(0))),
      _cargo()
    {}

    CCoordinate                    _coord;

    enum EKind {KLoad, KUnload, KDriveby};

    EKind                          _kind;
    boost::posix_time::time_period _timePeriod;
    std::vector<CCargo>            _cargo;
  };
  typedef std::vector<boost::shared_ptr<CShipmentStation> > TItinerary;

  struct CContract
  {
    unsigned int                  _id;
    //! whether some assignemnt for this contract was already suggested by an earliear run
    bool                          _wasSuggestedBefore;
    bool                          _sealed;
    //! cents
    unsigned int                  _price;
    CLoadAmmount                  _loadAmmount;
    TItinerary                    _itinerary;
  };

  struct CTruck
  {
    unsigned int                   _id;
    CCoordinate                    _homeStation;
    CLoadAmmount                   _capacity;
    //! cents
    unsigned int                   _priceKm;
    //! price per hour waiting, cents
    unsigned int                   _priceHour;
  };
}

#endif // SCHEDULER_ITINERARY_H
