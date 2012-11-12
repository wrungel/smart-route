#include "loadAmmount.h"

#include <algorithm>

namespace Scheduler
{
  CLoadAmmount GetMin(const CLoadAmmount& a, const CLoadAmmount& b)
  {
    return CLoadAmmount(std::min(a._weightKg, b._weightKg),
                        std::min(a._liter, b._liter),
                        std::min(a._units, b._units));
  }
}