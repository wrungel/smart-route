#ifndef SCHEDULER_SITUATION_H
#define SCHEDULER_SITUATION_H

#include <vector>

#include "truck.h"
#include "contract.h"
#include "route.h"


namespace Scheduler
{

  // data read from database, representing current situation, resp. input data
  struct CFramingData
  {
    std::vector<CTruck> _trucks;
    std::vector<CContract> _contracts;

    //! an entry for each truck
    std::vector<CTruckRoute> _truckRoutes;
  };
};

#endif // SCHEDULER_SITUATION_H