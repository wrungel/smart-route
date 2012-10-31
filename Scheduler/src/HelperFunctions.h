#ifndef SCHEDULER_HELPER_FUNCTIONS_H
#define SCHEDULER_HELPER_FUNCTIONS_H

#include <ctime>
#include "boost/date_time/posix_time/posix_time_types.hpp"

namespace Scheduler
{

 //! helper fuction converting boost::ptime to time_t
time_t To_time_t(boost::posix_time::ptime t)
{
   boost::posix_time::ptime epoch(boost::gregorian::date(1970,1,1));
   boost::posix_time::time_duration::sec_type x = (t - epoch).total_seconds();

    // ... check overflow here .. ?
   return time_t(x);
}

}

#endif // SCHEDULER_HELPER_FUNCTIONS_H