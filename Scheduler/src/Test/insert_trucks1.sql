INSERT INTO Account SET login="testdriver", passwd="123";
SET @accountId:=LAST_INSERT_ID();

INSERT INTO Driver SET account_id=@accountId;
SET @driverId:=LAST_INSERT_ID();

INSERT INTO Truck SET driver_id=@driverId, isAvailable=true, truckModel="MAN", truckIdNummer="8881",
   capacityUnits=5, capacityKg=3000, capacityM3=15.2, priceKm=1.1, priceHour=8.5, mobile="12345",
   addressHome="MusterStr.2, 12345 Berlin", zipHome="12345", latHome="51.241622", longHome="6.85277";

SET @truckId1=LAST_INSERT_ID();

INSERT INTO Truck SET driver_id=@driverId, isAvailable=true, truckModel="VW", truckIdNummer="8882",
   capacityUnits=5, capacityKg=2000, capacityM3=11.2, priceKm=0.95, priceHour=6.5, mobile="123",
   addressHome="MusterStr.8, 40444 Düsseldorf", zipHome="40444", latHome="52.489653", longHome="13.454837";

SET @truckId2=LAST_INSERT_ID();

INSERT INTO Route SET truck_id=@truckId2, isRealRoute=true;

SET @routeId2=LAST_INSERT_ID();

INSERT INTO RouteStation SET route_id=@routeId2, numberInSequence=1,
 latitude="50.879644", longitude="8.017845",
 kind='LOAD', timeFrom='2013-04-28 17:00:00', timeUntil='2013-04-29 13:00:00',
 leftKg=500, leftM3=3.8, leftUnits=0;

INSERT INTO RouteStation SET route_id=@routeId2, numberInSequence=2,
 latitude="50.879644", longitude="8.017845",
 kind='UNLOAD', timeFrom='2013-04-28 17:00:00', timeUntil='2013-04-29 13:00:00',
 leftKg=2000, leftM3=11.2, leftUnits=5;
