#ifndef SCHEDULER_SITUATION_H
#define SCHEDULER_SITUATION_H

#include <vector>

#include "truck.h"
#include "contract.h"
#include "route.h"

// structures used by scheduler search engine

namespace Scheduler
{
  struct CFramingData
  {
    std::vector<CTruck> _trucks;
    std::vector<CContract> _contracts;

     //! an entry for each truck
    std::vector<CTruckRoute> _truckRoutes;
  };
};

#endif // SCHEDULER_SITUATION_H