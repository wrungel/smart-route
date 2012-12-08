#ifndef SCHEDULER_COORDINATE_H
#define SCHEDULER_COORDINATE_H

namespace Scheduler
{
  struct CCoordinate
  {
    //! latitude * 10^6
    int _lat;
    //! longitude * 10^6
    int _long;
  };
}

#endif // SCHEDULER_COORDINATE_H