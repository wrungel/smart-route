#ifndef SCHEDULER_ITINERARY_HPP
#define SCHEDULER_ITINERARY_HPP

#include <vector>
#include <string>
#include <sstream>

#include "itineraryStation.h"

namespace Scheduler
{
  template <class taStation>
  class CItinerary : public std::vector<taStation>
  {
  public:
     std::string CheckTimePeriodsValidity() const; // empty string means its valid
  };

  template<class taStation>
  std::string CItinerary<taStation>::CheckTimePeriodsValidity() const
  {
    for (unsigned char i = 0; i < this->size(); ++i)
    {
      for (unsigned char j = i + 1; j < this->size(); ++j)
      {
        if ((*this)[i]->_timePeriod.is_after((*this)[j]->_timePeriod.end()))
        {
          std::stringstream exceptionText;
          exceptionText << "time period for station " << i << " is after tme period for station " << j;
          return exceptionText.str();
        }
      }
    }
    return "";
  }
}

#endif // SCHEDULER_ITINERARY_HPP
