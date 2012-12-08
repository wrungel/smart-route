#ifndef SCHEDULER_ROUTE_H
#define SCHEDULER_ROUTE_H

#include <set>

#include "itinerary.hpp"
#include "routeStation.h"

namespace Scheduler
{
  typedef std::set<TContractIndex> TContractSet;

   //! class representing a general route
  struct CRoute : public CItinerary<CRouteStation>
  {
    TContractSet _contracts;
  };

  //! class representing a route of a concrete truck
  struct CTruckRoute : public CItinerary<CTruckRouteStation>
  {
  };
}

#endif // SCHEDULER_ROUTE_H
