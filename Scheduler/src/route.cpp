#include "route.h"

#include <vector>
#include <algorithm>
#include <deque>

#include "mergingNode.hpp"
#include "distanceService.h"

using namespace Scheduler;


std::vector<CContract>* CRouteStation::pContracts = NULL;

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
TRouteVecPtr CRoute::MergeWith (const CRoute& anOtherRoute)
{
  TRouteVecPtr result(new boost::ptr_vector<CRoute>);

  typedef CMergingNode<CRoute, CRoute, CRoute> TNode;
  std::deque<TNode> agenda; // holding nodes to expand
  agenda.push_back(TNode(_sequence.begin(), anOtherRoute._sequence.begin(), new CRoute));
  while(!agenda.empty())
  {
    TNode& expandingNode = agenda.front();
    bool expandableByFirst = expandingNode._firstIterator != _sequence.end()
      &&  expandingNode._pValue->IsExtendibleByStation(*(expandingNode._firstIterator + 1)); // extendible by the next station in the first sequence
    bool expandableBySecond = expandingNode._secondIterator != anOtherRoute._sequence.end()
      &&  expandingNode._pValue->IsExtendibleByStation(*(expandingNode._secondIterator + 1)); // extendible by the next station in the second sequence
    bool bothWayExpandable = expandableByFirst && expandableBySecond;

    if (!expandableByFirst && !expandableBySecond)
    {
      // if route is complete combination, store it as result
      if (expandingNode._pValue->_sequence.size() == this->_sequence.size() + anOtherRoute._sequence.size())
      {
        result->push_back(expandingNode._pValue);
      }
      else
      {
        // clean up
        delete expandingNode._pValue;
      }
      // remove it from the agenda
      agenda.pop_front();
    }
    else if (bothWayExpandable)
    {
      // make a copy of node and of the route,and expand the copy, so we still have original route for expanding by second
      TNode newNode(expandingNode._firstIterator + 1,
                    expandingNode._secondIterator,
                    new CRoute(*(expandingNode._pValue)));
      bool firstSuccessful = newNode._pValue->ExtendByStation(*(newNode._firstIterator));

      // now alter the the 'original' route
      ++(expandingNode._secondIterator);
      bool secondSuccessful = expandingNode._pValue->ExtendByStation(*(expandingNode._secondIterator));

      if (!secondSuccessful)
      {
        delete expandingNode._pValue; // clean up
        agenda.pop_front();
      }

      if (!firstSuccessful)
      {
        delete newNode._pValue;
      }
      else
      {
        agenda.push_back(newNode);
      }
    }
    else
    {
      if (expandableByFirst)
      {
        ++(expandingNode._firstIterator);
        bool successful = expandingNode._pValue->ExtendByStation(*(expandingNode._firstIterator));
        if (!successful)
        {
          delete expandingNode._pValue; // clean up
          agenda.pop_front();
        }
      }
      else // expandableBySecond is true
      {
        ++(expandingNode._secondIterator);
        bool successful = expandingNode._pValue->ExtendByStation(*(expandingNode._secondIterator));
        if (!successful)
        {
          delete expandingNode._pValue; // clean up
          agenda.pop_front();
        }
      }
    }
  }

  return result;
}

bool CRoute::IsExtendibleByStation(const CRouteStation& aStationToAdd) const
{
  if (_sequence.size() == 0)
  {
    return true;
  }

  // 1) check 'sealed'
  const CRouteStation* lastStationWithContract = this->LastStationWithContract();
  if (lastStationWithContract != NULL)
  {
    const CContract* lastContract = lastStationWithContract->Contract();
    assert(lastContract);

    if (lastContract->_sealed)
    {
      // look whether the whole last contract is already in the route,
      // so it suffices to look for the last station of contract
      boost::shared_ptr<CShipmentStation> lastStationOfLastcontractPtr = lastContract->_sequence.back();
      bool contractInRoute = lastStationOfLastcontractPtr->_coord == lastStationWithContract->_shipmentStation->_coord;

      if (!contractInRoute)
      {
        return false;
      }
    }
  }

  // 2) distance check
  const CRouteStation& lastStation = this->_sequence.back();
  boost::posix_time::time_duration segmentTripDuration = CDistanceService::Instance().GetDistanceSeconds(lastStation->_coord, aStationToAdd->_coord);
  boost::posix_time::ptime earliestDeparture = lastStation->_timePeriod.begin() + CContract::KMinStationDuration;
  boost::posix_time::ptime latestArrival = aStationToAdd->_timePeriod.end() - CContract::KMinStationDuration;
  if (earliestDeparture + segmentTripDuration > latestArrival)
  {
    return false;
  }

  return true;
}

 // returns whtether extension was successful
bool CRoute::ExtendByStation(const CRouteStation& aStation)
{
  this->_sequence.push_back(aStation);
  // TODO: check whether the route still valid.
  bool stillValid = true;
  // If not, take the extension back
  if (!stillValid)
  {
    this->_sequence.erase(--(this->_sequence.end()));
  }

  return stillValid;
}

/*********** Combining truck routes
*/
CTruckRoute* CTruckRoute::MergeWith(const CRoute& aRoute)
{
  // TODO
  return 0;
}
