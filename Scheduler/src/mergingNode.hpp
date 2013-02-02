#ifndef SCHEDULER_MERGING_NODE_HPP
#define SCHEDULER_MERGING_NODE_HPP

#include <boost/type_traits.hpp>

#include <auto_ptr.h>

namespace Scheduler
{
  template <class taFirstItinerary, class taSecondItinerary, class taValueItinerary>
  struct CMergingNode
  {
    typedef typename taFirstItinerary::const_iterator TFirstIterator;
    typedef typename taSecondItinerary::const_iterator TSecondIterator;

    TFirstIterator     _firstIterator;
    TSecondIterator    _secondIterator;
    taValueItinerary*  _pValue;

    CMergingNode(TFirstIterator it1, TSecondIterator it2, taValueItinerary* value)
    : _firstIterator(it1), _secondIterator(it2), _pValue(value) {}

    // copy constructor
    CMergingNode(const CMergingNode& other): _firstIterator(other._firstIterator), _secondIterator(other._secondIterator), _pValue(other._pValue) {}
  };
}

#endif // SCHEDULER_MERGING_NODE_HPP