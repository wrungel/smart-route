#ifndef SCHEDULER_HELPER_FUNCTIONS_H
#define SCHEDULER_HELPER_FUNCTIONS_H

#include <string>
#include <ctime>
#include "boost/date_time/posix_time/posix_time_types.hpp"

#include <algorithm>
#include <functional>
#include <cctype>
#include <locale>

namespace Scheduler
{

 //! helper fuction converting boost::ptime to time_t
time_t To_time_t(boost::posix_time::ptime t);

int ParseDecimal(const std::string& toParse, unsigned char numAfterComma);


//! trim from start
static inline std::string &ltrim(std::string &s) {
        s.erase(s.begin(), std::find_if(s.begin(), s.end(), std::not1(std::ptr_fun<int, int>(std::isspace))));
        return s;
}

//! trim from end
static inline std::string &rtrim(std::string &s) {
        s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), s.end());
        return s;
}

//! trim from both ends
static inline std::string &trim(std::string &s) {
        return ltrim(rtrim(s));
}

}

#endif // SCHEDULER_HELPER_FUNCTIONS_H