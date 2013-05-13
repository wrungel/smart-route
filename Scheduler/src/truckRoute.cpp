#include "truckRoute.h"

#include <vector>
#include <algorithm>
#include <deque>

#include "mergingNode.hpp"
#include "distanceService.h"

using namespace Scheduler;

boost::posix_time::time_duration CTruckRoute::KLagToNextAppointment = boost::posix_time::minutes(30);

CTruckRoute::iterator CTruckRoute::FirstStationInFuture()
{
  boost::posix_time::ptime timeNow(boost::posix_time::second_clock::universal_time());
  boost::posix_time::ptime firstUnrestrictedTimepoint = timeNow + KLagToNextAppointment;
  for (CTruckRoute::iterator it = _sequence.begin(); it != _sequence.end(); ++it)
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
CTruckRoute* CTruckRoute::MergeWith(const CRoute& aRoute)
{
  typedef CMergingNode<CTruckRoute, CRoute, CTruckRoute> TNode;

  std::deque<TNode> agenda; // holding nodes to expand

  // find first changeable node TODO
  CTruckRoute::iterator firstStationInFuture = this->FirstStationInFuture();
  agenda.push_back(TNode(firstStationInFuture, aRoute._sequence.begin(), new CTruckRoute));


  return 0;
}