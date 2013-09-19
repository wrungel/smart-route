#ifndef SCHEDULER_SOLVER_H
#define SCHEDULER_SOLVER_H

#include <deque>
#include <map>

#include "boost/dynamic_bitset.hpp"

#include "situation.h"

namespace Scheduler
{
  class CSolver
  {
    public:
      void solve();

    private:
      struct TNode
      {
        CRoute* _route;
        boost::dynamic_bitset<> _truckVorbidden;

        TNode(size_t truckCount) : _route(), _truckVorbidden(truckCount, 0) {}
        TNode(CRoute* route, size_t truckCount) : _route(route), _truckVorbidden(truckCount, 0) {}
      };

      struct CTruckRouteAndContracts
      {
        CTruckRoute* _truckRoute;
        TContractSet _contracts;
      };

      struct TSolution
      {
        std::map<TTruckIndex, CTruckRouteAndContracts> _assignment;
      };
  };
}

#endif //SCHEDULER_SOLVER_H
