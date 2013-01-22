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

  struct CRoute; // forward decl.
  typedef std::auto_ptr<boost::ptr_vector<CRoute> > TRouteVecPtr;

   //! class representing a general route
  struct CRoute : public CItinerary<CRouteStation>, boost::noncopyable
  {
    TContractSet _contracts;

    //! create (0..N) routes as combination with a passed route
    TRouteVecPtr MergeWith (const CRoute& aRoute);
   };

  /*! create (0..N) routes from a order
  @contractIndex the contract index is stored in each route station (in order be able to identify original contract station)
  @contract
  */
  TRouteVecPtr CreateRoutes(TContractIndex contractIndex, const CContract& contract);

  //! class representing a route of a concrete truck
  struct CTruckRoute : public CItinerary<CTruckRouteStation>, boost::noncopyable
  {
    /*! construct a new TruckRoute by merging with a 'general' Route
        @return pointer to best constructed route, caller must handle memory.
        NULL if no feasible merging is possible */
    CTruckRoute* MergeWith(const CRoute& aRoute);
  };
}

#endif // SCHEDULER_ROUTE_H
