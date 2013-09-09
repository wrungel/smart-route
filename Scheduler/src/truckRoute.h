#ifndef SCHEDULER_TRUCK_ROUTE_H
#define SCHEDULER_TRUCK_ROUTE_H

#include "route.h"
#include <deque>
#include "mergingNode.hpp"

namespace Scheduler
{

  //! class representing a route of a concrete truck
  class CTruckRoute : public CItinerary<CTruckRouteStation>
  {
  public:
    //! an appointment (f.e. visit a route station) cannot be changed if the remaining time until it, being less than the following constant
    static boost::posix_time::time_duration KLagToNextAppointment;

    CTruckRoute() : CItinerary<CTruckRouteStation>() {}
    CTruckRoute(const CTruckRoute& aRoute) : CItinerary<CTruckRouteStation>(aRoute) {}
    CTruckRoute(const_iterator first, const_iterator last) : CItinerary<CTruckRouteStation>(first, last) {}

    /*! construct a new TruckRoute by merging with a 'general' Route
        @return pointer to best constructed route (caller must handle memory), NULL if no feasible merging is possible */
    CTruckRoute* MergeWith(const CRoute& aRoute) const;

  private:
    //! finds first station, which lays in future, meaning the truck has not visited it yet
    CTruckRoute::const_iterator FirstStationInFuture() const;

    CTruckRoute::const_iterator LastFixedStationInFuture() const;

    bool IsExtendibleByStation(const CRouteStation& aStation) const;
    bool IsExtendibleByStation(const CTruckRouteStation& aStation) const;

    // returns whtether extension was successful
    bool ExtendByStation(const CRouteStation& aStation);
    bool ExtendByStation(const CTruckRouteStation& aStation);

    typedef CMergingNode<CTruckRoute, CRoute, CTruckRoute> TNode;
        // do the extension by second and push it into agenda
    void DoExtensionBySecond(TNode& expandingNode, std::deque<TNode>& agenda) const;

    // do the extension by first and push it into agenda
    void DoExtensionByFirst(TNode& expandingNode, std::deque<TNode>& agenda) const;
  };
}

#endif // SCHEDULER_TRUCK_ROUTE_H