#include "HelperFunctions.h"

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

int ParseDecimal(const std::string& toParse, unsigned char numAfterComma)
{
  std::size_t delimiterPos = toParse.find('.');
  int beforeComma = atoi(toParse.substr(0, delimiterPos).c_str());
  if (numAfterComma == 0)
  {
    return beforeComma;
  }

  int scaleFactor = 1;
  for(unsigned char i = 1; i < numAfterComma; i++)
  {
    scaleFactor *= 10;
  }
  beforeComma *= scaleFactor * 10;

  if (std::string::npos == delimiterPos) //no delimieter (comma) found
  {
    return beforeComma;
  }

  std::string afterCommaStr = toParse.substr(delimiterPos + 1, numAfterComma);
  unsigned int afterComma = atoi(afterCommaStr.c_str());
  if (afterComma > 0)
  {
    while (afterComma < scaleFactor) {afterComma *= 10;}
  }

  if (toParse.find('-') == 0) // starts with '-', so its negative
  {
    return beforeComma - afterComma;
  }
  else
  {
    return beforeComma + afterComma;
  }
}

}