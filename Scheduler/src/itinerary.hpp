#ifndef SCHEDULER_ITINERARY_HPP
#define SCHEDULER_ITINERARY_HPP

#include <vector>
#include <string>
#include <sstream>

namespace Scheduler
{
  template <class taStation>
  struct CItinerary
  {
    public:
     typedef typename std::vector<taStation> TSequence;
     typedef typename TSequence::iterator iterator;
     typedef typename TSequence::const_iterator const_iterator;
     typedef typename TSequence::reverse_iterator reverse_iterator;
     std::string CheckTimePeriodsValidity() const; // empty string means its valid

     TSequence _sequence;
     const taStation* LastStationWithContract() const;
  };

  template<class taStation>
  std::string CItinerary<taStation>::CheckTimePeriodsValidity() const
  {
    for (unsigned char i = 0; i < _sequence.size(); ++i)
    {
      for (unsigned char j = i + 1; j < _sequence.size(); ++j)
      {
        if (_sequence[i]->_timePeriod.is_after(_sequence[j]->_timePeriod.end()))
        {
          std::stringstream exceptionText;
          exceptionText << "time period for station " << i << " is after tme period for station " << j;
          return exceptionText.str();
        }
      }
    }
    return "";
  }

  template<class taStation>
  const taStation* CItinerary<taStation>::LastStationWithContract() const
  {
    for (int i = _sequence.size() - 1; i >= 0 ; i--)
    {
      if (_sequence[i]._contractIndex >= 0)
      {
        return &(_sequence[i]);
      }
    }
    return NULL;
  }
}

#endif // SCHEDULER_ITINERARY_HPP
