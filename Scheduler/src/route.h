#ifndef SCHEDULER_ROUTE_H
#define SCHEDULER_ROUTE_H

#include <set>

#include <boost/noncopyable.hpp>
#include <boost/ptr_container/ptr_vector.hpp>

#include "itinerary.hpp"
#include "routeStation.h"

namespace Scheduler
{
  typedef std::set<TContractIndex> TContractSet;

   //! class representing a general route
  struct CRoute : public CItinerary<CRouteStation>, boost::noncopyable
  {
    TContractSet _contracts;
   };

  // one or morw routes
  std::auto_ptr<boost::ptr_vector<CRoute> > CreateRoutes(TContractIndex contractIndex, const CContract& contract);

  //! class representing a route of a concrete truck
  struct CTruckRoute : public CItinerary<CTruckRouteStation>
  {
  };
}

#endif // SCHEDULER_ROUTE_H
