#include "route.h"

#include <vector>
#include <algorithm>
#include <deque>

#include "mergingNode.hpp"

using namespace Scheduler;


typedef CContract::TStationIndex TStationIndex;

// a helper structure
struct SStationRep
{
    TStationIndex _index;
    const CContract* _contract;

    SStationRep() : _index(0), _contract(0) {}
    SStationRep(TStationIndex index, const CContract& contract)
    : _index(index), _contract(&contract) {}

    inline bool operator==(const SStationRep& rhs) const
    {
      return   _contract->AMustbeBeforeB(this->_index, rhs._index)
            || _contract->AMustbeBeforeB(rhs._index, this->_index);
    }

    inline bool operator<(const SStationRep& rhs) const
    {
      if ((*this) == rhs)
      {
        return false;
      }
      return this->_index < rhs._index;
    }
};

// creating (general) route(s) from a contract
std::auto_ptr<boost::ptr_vector<CRoute> > CreateRoutes(TContractIndex contractIndex,
                                                       const CContract& contract)
{
  std::auto_ptr<boost::ptr_vector<CRoute> > result(new boost::ptr_vector<CRoute>);

  // following vector represents a station ordering
  // it will be transformed via next_permutation() in order iterate over all feasible station orderings
  std::vector<SStationRep> repSequence;
  for (TStationIndex i = 0; i < contract._sequence.size(); i++)
  {
    repSequence.push_back(SStationRep(i, contract));
  }

  // now the iteration, for each feasible station ordering a route is created
  do{
    CRoute* pRoute = new CRoute();
    for (std::vector<SStationRep>::iterator it = repSequence.begin(); it != repSequence.end() ; ++it)
    {
      pRoute->_sequence.push_back(CRouteStation(contractIndex, contract._sequence[it->_index]));
    }
    result->push_back(pRoute);
  }
  while (std::next_permutation(repSequence.begin(), repSequence.end()));

  return result;
}

/*********** Combining general routes
*/
//! create (0..N) routes as combination with a passed route
TRouteVecPtr CRoute::MergeWith (const CRoute& aRoute)
{
  TRouteVecPtr result(new boost::ptr_vector<CRoute>);
  CRoute* pRoute = new CRoute();

  typedef CMergingNode<CRoute, CRoute, CRoute> TMergingNode;
  std::deque<TMergingNode> agenda;
  agenda.push_back(TMergingNode(_sequence.begin(), aRoute._sequence.begin(), pRoute));

  return result;
}

/*********** Combining truck routes
*/
CTruckRoute* CTruckRoute::MergeWith(const CRoute& aRoute)
{
  return 0;
}
