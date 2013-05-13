#include "truckRoute.h"

#include <vector>
#include <algorithm>
#include <deque>

#include "mergingNode.hpp"
#include "distanceService.h"

using namespace Scheduler;

boost::posix_time::time_duration CTruckRoute::KLagToNextAppointment = boost::posix_time::minutes(30);

CTruckRoute::const_iterator CTruckRoute::FirstStationInFuture() const
{
  boost::posix_time::ptime timeNow(boost::posix_time::second_clock::universal_time());
  boost::posix_time::ptime firstUnrestrictedTimepoint = timeNow + KLagToNextAppointment;
  for (CTruckRoute::const_iterator it = _sequence.begin(); it != _sequence.end(); ++it)
  {
    if (it->_plannedTimePeriod.begin() >= firstUnrestrictedTimepoint)
    {
      return it;
    }
  }
  return _sequence.end();
}


/*********** Combining truck routes
*/
CTruckRoute* CTruckRoute::MergeWith(const CRoute& aRoute) const
{
  typedef CMergingNode<CTruckRoute, CRoute, CTruckRoute> TNode;

  std::deque<TNode> agenda; // holding nodes to expand
  CTruckRoute::const_iterator firstStationInFuture = this->FirstStationInFuture();
  // stations 'in the past' are simply copied

  CTruckRoute* pConstructingRoute = new CTruckRoute(this->_sequence.begin(), firstStationInFuture);
  agenda.push_back(TNode(firstStationInFuture, aRoute._sequence.begin(), pConstructingRoute));


  return 0;
}