#ifndef SCHEDULER_COORDINATE_H
#define SCHEDULER_COORDINATE_H

#include <limits>

namespace Scheduler
{
  struct CCoordinate
  {
    //! latitude * 10^6  (KScaleFactor)
    int _lat;
    //! longitude * 10^6  (KScaleFactor)
    int _long;

    static const unsigned char KDigitsAfterComma = 6;
    static const int KScaleFactor = 1000000; // 10^6

    static bool IsValidLatitude(int val) { return val <= 90 * KScaleFactor && val >= -90 * KScaleFactor; }
    static bool IsValidLongitude(int val) { return val <= 180 * KScaleFactor && val >= -180 * KScaleFactor; }

    CCoordinate() : _lat(std::numeric_limits<int>::min()), _long(std::numeric_limits<int>::min()) {}

    bool IsValid() { return IsValidLatitude(_lat) && IsValidLongitude(_long); }
  };
}

#endif // SCHEDULER_COORDINATE_H