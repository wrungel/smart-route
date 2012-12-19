#ifndef SCHEDULER_HELPER_FUNCTIONS_H
#define SCHEDULER_HELPER_FUNCTIONS_H

#include <string>
#include <ctime>
#include "boost/date_time/posix_time/posix_time_types.hpp"

namespace Scheduler
{

 //! helper fuction converting boost::ptime to time_t
time_t To_time_t(boost::posix_time::ptime t);

int ParseDecimal(const std::string& toParse, unsigned char numAfterComma);

}

#endif // SCHEDULER_HELPER_FUNCTIONS_H