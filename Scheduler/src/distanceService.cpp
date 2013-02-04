#include <cmath>

#include "distanceService.h"

using namespace Scheduler;

CDistanceService::CDistanceService()
  : _cache()
{
  // TODO: populate from persistent source
}

unsigned int CDistanceService::EstimateDistance(const CCoordinate& from, const CCoordinate& to) const
{
  static double metersInDegree = 10000; // asume grad = 10000 meter
  static double scale = CCoordinate::KScaleFactor / metersInDegree;
  double dx = (to._lat - from._lat) / scale;
  double dy = (to._long - from._long) / scale;
  double distanceMeters = sqrt(dx*dx + dy*dy);

  static double speed = 25; // 25 m/sec = 90 km/h
  double seconds = (distanceMeters / speed) * 1.5; // factor 1.5 to weigh out that distans is as the bird flies.
  return seconds;
}

unsigned int CDistanceService::GetDistanceSeconds(const CCoordinate& from, const CCoordinate& to)
{
  CDistanceService::TKey key (from, to);
  TCache::iterator it = _cache.find(key);
  if (it != _cache.end())
  {
    return it->second;
  }

  // we didnt find in a cache. Lets compute the distance
  // TODO: replace this with router call !
  unsigned int distance = EstimateDistance(from, to);

  // add computed value into the cache
  _cache[key] = distance;

  // TODO: save new value persistently?

  return distance;
}