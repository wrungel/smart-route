#ifndef SCHEDULER_TRUCK_ASSIGNMENT_STATUS_H
#define SCHEDULER_TRUCK_ASSIGNMENT_STATUS_H

#include <bitset>
#include <vector>
#include "itinerary.h"

#include <boost/assert.hpp>

namespace Scheduler
{
  struct TAssignmentType
  {
    public: // getter
      bool IsReturnJourney() const { return _flags[KReturnJourney]; }
      bool IsTakeWith() const { return _flags[KTakeWith]; }
      bool IsCombination() const { return _flags[KCombination]; }
      bool IsSemiReturnJourney() const { return _flags[KSemiReturnJourney]; }
      bool IsFromHomeStation() const { return _flags[KFromHomeStation]; }

    public: // setter
      TAssignmentType& SetReturnJourney(bool flag) { _flags[KReturnJourney] = flag; return *this;}
      TAssignmentType& SetTakeWith(bool flag) { _flags[KTakeWith] = flag; return *this;}
      TAssignmentType& SetCombination(bool flag) { _flags[KCombination] = flag; return *this;}
      TAssignmentType& SetSemiReturnJourney(bool flag) { _flags[KSemiReturnJourney] = flag; return *this;}
      TAssignmentType& SetFromHomeStation(bool flag) { _flags[KFromHomeStation] = flag; return *this;}

    protected:
      typedef std::bitset<8> TFlags;

      enum EBits {
        KReturnJourney = 7,      // the contract is assigned for the return journey to the home-station
        KTakeWith = 6,           // the contract is to acomplish "on-the-way" (together with some other contract done by the truck)
        KCombination = 5,        // assigned in combination with some other contract (multiple cotracts at once to the same truck)
        KSemiReturnJourney = 4,  // the contract is assigned for the conditional return journey (still quite far away to the home-station)
        KFromHomeStation = 3     // assigned because the contract trip starts near home_station
      };

      TFlags _flags;
  };

  class CAssignmentStatus : public TAssignmentType
  {
    public: // getter
      //! the truck cannot be asigned to the contract
      bool IsVorbidden() const { BOOST_ASSERT(!IsAssigned()); return _flags[KVorbidden]; }
      //! the truck is assigned to the contract
      bool IsAssigned() const { return _flags[KAssigned]; }
      //! the assignemnt was already suggested by an earliear run
      bool WasSuggestedBefore() const { return _flags[KSuggestedBefore]; }

    public: // setter
      CAssignmentStatus& SetVorbidden(bool flag)
      {
        _flags[KVorbidden] = flag;
        if (flag) {_flags[KAssigned] = false; }
        return *this;
      }
      CAssignmentStatus& SetAssigned(bool flag) { _flags[KAssigned] = flag; return *this; }
      CAssignmentStatus& SetWasSuggestedBefore(bool flag) { _flags[KSuggestedBefore] = flag; return *this; }

    protected:
      enum EBits {
        KReturnJourney = 7,      // the contract is assigned for the return journey to the home-station
        KTakeWith = 6,           // the contract is to acomplish "on-the-way" (together with some other contract done by the truck)
        KCombination = 5,        // assigned in combination with some other contract (multiple cotracts at once to the same truck)
        KSemiReturnJourney = 4,  // the contract is assigned for the conditional return journey (still quite far away to the home-station)
        KFromHomeStation = 3,     // assigned because the contract trip starts near home_station

        KVorbidden = 0,
        KAssigned = 1,
        KSuggestedBefore = 2
      };
  };
}

#endif // TRUCK_ASSIGNMENT_STATUS_H