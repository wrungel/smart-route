#ifndef SCHEDULER_SEARCH_ENGINE_H
#define SCHEDULER_SEARCH_ENGINE_H

#include "situation.h" //TODO: rename

namespace Scheduler
{
  //    TODO: zoning
  class CZoneSet
  {
  };

  std::vector<CZoneSet> zonesForContract;


  class CContractSet
  {
    // TODO: a) bitset of a run-time-known size (contracts.size())
    // TODO: b) std::vector<TContractIndex>
    // TODO: operator Union and operator Schnitt
  };

  //! represents solutions in computation for single truck
  class CNodeForTruck
  {
    TRoute _route;
    CContractSet _contracts;
    CZoneSet _zones;
  };

  // TODO: priority queue

  // TODO: result container
}

#endif // SCHEDULER_SEARCH_ENGINE_H