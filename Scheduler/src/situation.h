#ifndef SCHEDULER_SITUATION_H
#define SCHEDULER_SITUATION_H

#include <vector>
#include "itinerary.h"

// structures used by scheduler search engine

namespace Scheduler
{
  typedef signed char TContractIndex;
  typedef int TTruckIndex;

  // ! class representing a station on route of a concrete truck
  struct CRouteStation
  {
    //! the contract is in database but not loaded into scheduler
    static const TContractIndex KUnloadedContract = -1;
    //! route station without contract
    static const TContractIndex KNoContract = -2;

    TContractIndex _contractIndex;

    boost::shared_ptr<CShipmentStation> _station;

    //! truck capacity after performing the operation (load / unload / nop) on the station
    CLoadAmmount _capacity;
  };

  typedef std::vector<CRouteStation> TRoute;

  struct CFramingData
  {
    std::vector<CTruck> _trucks;
    std::vector<CContract> _contracts;

     //! an entry for each truck
    std::vector<TRoute> _truckRoute;
  };
};

#endif // SCHEDULER_SITUATION_H