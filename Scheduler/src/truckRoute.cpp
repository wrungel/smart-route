#include "truckRoute.h"

#include <vector>
#include <algorithm>

#include "distanceService.h"

using namespace Scheduler;

boost::posix_time::time_duration CTruckRoute::KLagToNextAppointment = boost::posix_time::minutes(30);

CTruckRoute::const_iterator CTruckRoute::FirstStationInFuture() const
{
  boost::posix_time::ptime timeNow(boost::posix_time::second_clock::universal_time());
  boost::posix_time::ptime firstUnrestrictedTimepoint = timeNow + KLagToNextAppointment;
  for (CTruckRoute::const_iterator it = _sequence.begin(); it != _sequence.end(); ++it)
  {
    if (it->_plannedTimePeriod.begin() >= firstUnrestrictedTimepoint)
    {
      return it;
    }
  }
  return _sequence.end();
}

CTruckRoute::const_iterator CTruckRoute::LastFixedStationInFuture() const
{
  boost::posix_time::ptime timeNow(boost::posix_time::second_clock::universal_time());
  boost::posix_time::ptime firstUnrestrictedTimepoint = timeNow + KLagToNextAppointment;
  for (CTruckRoute::const_iterator it = _sequence.begin(); it != _sequence.end(); ++it)
  {
    if (it->_plannedTimePeriod.begin() >= firstUnrestrictedTimepoint)
    {
      return it;
    }
  }
  return _sequence.end();
}


/*********** Combining truck routes ************/

// do the extension by second and push it into agenda
void CTruckRoute::DoExtensionBySecond(TNode& expandingNode, std::deque<TNode>& agenda) const
{
      CTruckRoute* extendedBySecond = new CTruckRoute(*(expandingNode._pValue));
      bool extensionSuccessful = extendedBySecond->ExtendByStation(*(expandingNode._secondIterator));
      if (extensionSuccessful)
      {
         const bool preferFirst = false;
         TNode newNode(expandingNode._firstIterator, expandingNode._secondIterator + 1, extendedBySecond, preferFirst);
         agenda.push_back(newNode);
      }
      else
      {
        delete extendedBySecond;
      }
}

// do the extension by first and push it into agenda
void CTruckRoute::DoExtensionByFirst(TNode& expandingNode, std::deque<TNode>& agenda) const
{
      CTruckRoute* extendedByFirst = new CTruckRoute(*(expandingNode._pValue));
      bool extensionSuccessful = extendedByFirst->ExtendByStation(*(expandingNode._firstIterator));
      if (extensionSuccessful)
      {
         const bool preferFirst = true;
         TNode newNode(expandingNode._firstIterator + 1, expandingNode._secondIterator, extendedByFirst, preferFirst);
         agenda.push_back(newNode);
      }
      else
      {
        delete extendedByFirst;
      }
}

CTruckRoute* CTruckRoute::MergeWith(const CRoute& aRoute) const
{
  std::deque<TNode> agenda; // holding nodes to expand
  CTruckRoute::const_iterator firstStationInFuture = this->FirstStationInFuture();

  // stations 'in the past' are simply copied
  CTruckRoute* pConstructingRoute = new CTruckRoute(this->_sequence.begin(), firstStationInFuture);
  pConstructingRoute->_sequence.reserve(this->_sequence.size() + aRoute._sequence.size());

  agenda.push_back(TNode(firstStationInFuture, aRoute._sequence.begin(), pConstructingRoute, true));

  CTruckRoute* result = NULL;

  while( !agenda.empty())
  {
    TNode& expandingNode = agenda.front();

    // deep first search
    // after station of truck route we prefer again a station of truck route
    // after station of general route we prefer again a station of general route

    bool extensionBySecondSuccessful = true;
    bool extensionByFirstSuccessful = true;

    do{
    if (expandingNode._preferFirst)
    {
      while (extensionByFirstSuccessful
        && expandingNode._firstIterator != this->_sequence.end()
        && (result == NULL // comparing with the lower bound like in branch-and-bound algorithm
         || expandingNode._pValue->_sequence.back()._plannedTimePeriod.end()
            < result->_sequence.back()._plannedTimePeriod.end())
        && expandingNode._pValue->IsExtendibleByStation(*(expandingNode._firstIterator)))
      {
        if (expandingNode._secondIterator != aRoute._sequence.end())
        {
          DoExtensionBySecond(expandingNode, agenda);
        }

        // now do the extension by first
        extensionByFirstSuccessful = expandingNode._pValue->ExtendByStation(*(expandingNode._firstIterator));
        if (extensionByFirstSuccessful)
        {
          extensionBySecondSuccessful = true; // reset this, since one extension by first was successful
          expandingNode._firstIterator++;
        }
      }

      // next turn is a series of extensions by second
      expandingNode._preferFirst = false;
    }
    else
    {
      while (extensionBySecondSuccessful
        && expandingNode._secondIterator != aRoute._sequence.end()
        && (result == NULL // comparing with the lower bound like in branch-and-bound algorithm
         || expandingNode._pValue->_sequence.back()._plannedTimePeriod.end()
            < result->_sequence.back()._plannedTimePeriod.end())
        && expandingNode._pValue->IsExtendibleByStation(*(expandingNode._secondIterator)))
      {
        if (expandingNode._firstIterator != this->_sequence.end())
        {
          DoExtensionByFirst(expandingNode, agenda);
        }

        // now do the extension by second
        extensionBySecondSuccessful = expandingNode._pValue->ExtendByStation(*(expandingNode._firstIterator));
        if (extensionBySecondSuccessful)
        {
          extensionByFirstSuccessful = true; // reset this, since one extension by second was successful
          expandingNode._firstIterator++;
        }
      }

      // next turn is a series of extensions by first
      expandingNode._preferFirst = true;
    }

    }
    while ((extensionByFirstSuccessful || extensionBySecondSuccessful)
      && (result == NULL  // comparing with the lower bound like in branch-and-bound algorithm
         || expandingNode._pValue->_sequence.back()._plannedTimePeriod.end()
            < result->_sequence.back()._plannedTimePeriod.end())
      && (expandingNode._pValue->_sequence.size() < this->_sequence.size() + aRoute._sequence.size()));

    if (expandingNode._pValue->_sequence.size() == this->_sequence.size() + aRoute._sequence.size())
    {
      if (result == NULL || result->_sequence.back()._plannedTimePeriod.end() >
        expandingNode._pValue->_sequence.back()._plannedTimePeriod.end())
      {
        delete result;
        result = expandingNode._pValue;
      }
      else
      {
        delete expandingNode._pValue;
      }
    }
    else
    {
      delete expandingNode._pValue;
    }
  }

  return result;
}

bool CTruckRoute::IsExtendibleByStation(const CRouteStation& aStation) const
{
  return true;
}

bool CTruckRoute::IsExtendibleByStation(const CTruckRouteStation& aStation) const
{
  return true;
}

// returns whtether extension was successful
bool CTruckRoute::ExtendByStation(const CRouteStation& aRouteStation)
{
  CTruckRouteStation station(aRouteStation);
  //TODO: compute   station._plannedTimePeriod and  station._remainingTruckCapacity
  return ExtendByStation(station);
}

// returns whtether extension was successful
bool CTruckRoute::ExtendByStation(const CTruckRouteStation& aStation)
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

