#ifndef SCHEDULER_ROUTE_STATION_H
#define SCHEDULER_ROUTE_STATION_H

#include "contract.h"
#include "shipmentStation.h"
#include "loadAmmount.h"

namespace Scheduler
{
  //! class representing a station on a general route .. which is possibly combined from multiple contracts
  struct CRouteStation
  {
    //! the contract is in database but not loaded into scheduler
    static const TContractIndex KUnloadedContract = -1;
    //! route station without contract
    static const TContractIndex KNoContract = -2;

    TContractIndex _contractIndex;

    boost::shared_ptr<CShipmentStation> _station;

    CShipmentStation* operator-> () { return _station.operator->(); }
    CShipmentStation& operator* ()  { return _station.operator*(); }
  };

  //! class representing a station on a route specific for a single truck
  struct CTruckRouteStation : public CRouteStation
  {
    CLoadAmmount _remainingTruckCapacity;
  };
}
#endif // SCHEDULER_ROUTE_STATION_H