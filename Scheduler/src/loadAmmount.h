#ifndef SCHEDULER_LOAD_AMMOUNT_H
#define SCHEDULER_LOAD_AMMOUNT_H

#include <limits>
#include <string>

namespace Scheduler
{
  struct CLoadAmmount
  {
    CLoadAmmount() : _weightKg(0), _liter(0), _units(0) {}
    CLoadAmmount(unsigned int weightKg, unsigned int liter, unsigned short units)
     : _weightKg(weightKg), _liter(liter), _units(units) {}

    unsigned int _weightKg;
    unsigned int _liter;       // volume
    unsigned short _units; // paletten

    void SetMaximumValues()
    {
      _weightKg = std::numeric_limits<unsigned int>::max();
      _liter = std::numeric_limits<unsigned int>::max();
      _units = std::numeric_limits<unsigned short>::max();
    }
  };

  CLoadAmmount GetMin(const CLoadAmmount& a, const CLoadAmmount& b);

  struct CCargo : public CLoadAmmount
  {
    CCargo() : CLoadAmmount(), _cargoType("") {}
    CCargo(unsigned int weightKg, unsigned int liter, unsigned short units, const std::string& cargoType)
      : CLoadAmmount(weightKg, liter, units), _cargoType(cargoType) {}
    std::string _cargoType;
  };
}

#endif // SCHEDULER_LOAD_AMMOUNT_H