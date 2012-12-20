#ifndef SCHEDULER_CCONTRACT_H
#define SCHEDULER_CCONTRACT_H

#include "boost/shared_ptr.hpp"

#include "shipmentStation.h"

#include "itinerary.hpp"

namespace Scheduler
{
  struct CContract
  {
    unsigned int                  _id;
    //! whether some assignemnt for this contract was already suggested by an earliear run
    bool                          _wasSuggestedBefore;
    bool                          _sealed;
    //! cents
    unsigned int                  _price;
    CLoadAmmount                  _loadAmmount; //total

    typedef CItinerary<boost::shared_ptr<CShipmentStation> > TStationSequence;
    TStationSequence              _stationSequence;

    CContract() : _id(0), _wasSuggestedBefore(false), _sealed(false), _price(0), _loadAmmount(), _stationSequence() {}
  };

  typedef signed char TContractIndex;
}

#endif // SCHEDULER_CCONTRACT_H