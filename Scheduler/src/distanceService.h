#ifndef SCHEDULER_DISTANCE_SERVICE_H
#define SCHEDULER_DISTANCE_SERVICE_H

#include "boost/date_time/posix_time/posix_time_types.hpp"

#include <utility>
#include <boost/noncopyable.hpp>
#include <boost/unordered/unordered_map.hpp>

#include "coordinate.h"

namespace Scheduler
{
  /*! the DistanceService maintaines the Cache of Distances. Is a requested distance is not in the cache, it consults the router.
      It encapsulates the access to the router (TODO)
   */
  class CDistanceService : public boost::noncopyable // singleton
  {
  public:
    static CDistanceService& Instance()
    {
      static CDistanceService instance;
      return instance;
    }

    // distance in seconds
    unsigned int GetDistanceSeconds(const CCoordinate& from, const CCoordinate& to);
    // distance as time interval
    boost::posix_time::time_duration GetDistance(const CCoordinate& from, const CCoordinate& to)
    {
      return boost::posix_time::seconds(GetDistanceSeconds(from, to));
    }

  private:
    CDistanceService(); // singleton

    unsigned int EstimateDistance(const CCoordinate& from, const CCoordinate& to) const;

    typedef std::pair<CCoordinate, CCoordinate> TKey;
    // folowing defines hash function for CCoordinate pair
    struct Hash : std::unary_function<TKey, std::size_t>
    {
      std::size_t operator()(const TKey& aKey) const
      {
        std::size_t seed = 0;
        boost::hash_combine( seed, aKey.first._lat);
        boost::hash_combine( seed, aKey.first._long);
        boost::hash_combine( seed, aKey.second._lat);
        boost::hash_combine( seed, aKey.second._long);
        return seed;
      }
    };

    typedef boost::unordered_map<TKey, unsigned int, Hash> TCache;
    TCache _cache;
  };
}

#endif // SCHEDULER_DISTANCE_SERVICE_H