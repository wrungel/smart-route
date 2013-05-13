#ifndef SCHEDULER_TRUCK_ROUTE_H
#define SCHEDULER_TRUCK_ROUTE_H

#include "route.h"

namespace Scheduler
{

  //! class representing a route of a concrete truck
  class CTruckRoute : public CItinerary<CTruckRouteStation>
  {
  public:
    //! an appointment (f.e. visit a route station) cannot be changed if remaining time till it is less than the following constant
    static boost::posix_time::time_duration KLagToNextAppointment;

    CTruckRoute() : CItinerary<CTruckRouteStation>() {}
    CTruckRoute(const CTruckRoute& aRoute) : CItinerary<CTruckRouteStation>(aRoute) {}
    CTruckRoute(const_iterator first, const_iterator last) : CItinerary<CTruckRouteStation>(first, last) {}

    /*! construct a new TruckRoute by merging with a 'general' Route
        @return pointer to best constructed route (caller must handle memory), NULL if no feasible merging is possible */
    CTruckRoute* MergeWith(const CRoute& aRoute) const;

    //! finds first station, which lays in future, meaning the truck has not visited it yet
    CTruckRoute::const_iterator FirstStationInFuture() const;
  };
}

#endif // SCHEDULER_TRUCK_ROUTE_H