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

    static std::vector<CContract>* pContracts;

    TContractIndex _contractIndex;
    boost::shared_ptr<CShipmentStation> _shipmentStation;

    CRouteStation() : _contractIndex(KNoContract), _shipmentStation() {}
    CRouteStation(TContractIndex index, CShipmentStation* station) : _contractIndex(index), _shipmentStation(station) {}
    CRouteStation(TContractIndex index, const boost::shared_ptr<CShipmentStation>& sharedStation) : _contractIndex(index), _shipmentStation(sharedStation) {}

    CShipmentStation* operator-> () { return _shipmentStation.operator->(); }
    const CShipmentStation* operator-> () const { return _shipmentStation.operator->(); }
    CShipmentStation& operator* ()  { return _shipmentStation.operator*(); }
    const CShipmentStation& operator* () const { return _shipmentStation.operator*(); }

    const CContract* Contract() const
    {
       if (!IsContractIndexValid(_contractIndex))
         return NULL;
       else
         return &((*pContracts)[_contractIndex]);
    }

    private:
    static bool IsContractIndexValid(TContractIndex aIndex) { return aIndex >=0 && pContracts && static_cast<size_t>(aIndex) < pContracts->size();}
 };

  //! class representing a station on a route specific for a single truck
  struct CTruckRouteStation : public CRouteStation
  {
    //! capacity after visiting  this station
    CLoadAmmount _remainingTruckCapacity;

    //! when for a concrete truck is planned to be there, its a sub-period of station._timePeriod,
    boost::posix_time::time_period _plannedTimePeriod;

    CTruckRouteStation()
    :
     CRouteStation(),
     _remainingTruckCapacity(),
     _plannedTimePeriod(DefaultTimePeriod())
    {}
  };
}
#endif // SCHEDULER_ROUTE_STATION_H