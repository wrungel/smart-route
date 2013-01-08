#include "contract.h"

#include <boost/assert.hpp>
#include <boost/concept_check.hpp>

using namespace Scheduler;

boost::posix_time::time_duration CContract::KMinStationDuration = boost::posix_time::minutes(15);

bool CContract::AMustbeBeforeB(TStationIndex indexA, TStationIndex indexB) const
{
  BOOST_ASSERT(indexA < _stationSequence.size());
  BOOST_ASSERT(indexB < _stationSequence.size());

  if (indexA == indexB)
  {
    return false;
  }

  const CShipmentStation& stationA = *(_stationSequence[indexA]);
  const CShipmentStation& stationB = *(_stationSequence[indexB]);

  if (stationA._kind == CShipmentStation::KLoad && stationB._kind == CShipmentStation::KUnload)
  {
    return true;
  }

  if (stationA._timePeriod.is_before(stationB._timePeriod.begin() + KMinStationDuration))
  {
    return true;
  }

  return false;
}