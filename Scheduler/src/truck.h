#ifndef SCHEDULER_TRUCK_H
#define SCHEDULER_TRUCK_H

#include "coordinate.h"
#include "loadAmmount.h"

namespace Scheduler
{
  struct CTruck
  {
    unsigned int                   _id;
    CCoordinate                    _homeStation;
    CLoadAmmount                   _capacity;
    //! cents
    unsigned int                   _priceKm;
    //! price per hour waiting, cents
    unsigned int                   _priceHour;
  };

  typedef int TTruckIndex;
}

#endif // SCHEDULER_TRUCK_H