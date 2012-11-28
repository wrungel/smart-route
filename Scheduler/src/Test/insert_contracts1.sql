INSERT INTO Account SET login="testcustomer", passwd="123";
SET @accountId:=LAST_INSERT_ID();

INSERT INTO Customer SET account_id=@accountId;
SET @customerId:=LAST_INSERT_ID();

INSERT INTO Contract SET toBeAssigned=true, sealed=true,
   price=999.99,
   entireWeightKg=1000, entireVolumeM3=3.2, entireVolumeUnits=5,
   reception=NOW(),
   decayTime='2013-05-01 12:00:00',
   customer_id=@customerId;
SET @contrId:=LAST_INSERT_ID();

INSERT INTO ContractStation SET contract_id=@contrId, numberInSequence=2, kind="unload",
   timeFrom='2013-05-03 12:00:00', timeUntil='2013-05-03 18:00:00',
   address="Heinrich-Könn-Str. 265 40625 Düsseldorf", zip="40265", latitude=51.241622, longitude=6.85277;
SET @arrivalId:=LAST_INSERT_ID();

INSERT INTO ContractStation SET contract_id=@contrId, numberInSequence=1, kind="load",
   timeFrom='2013-05-02 12:00:00', timeUntil='2013-05-02 18:00:00', address="Elsenstr.105 12435 Berlin",
   zip="12435", latitude=52.489653, longitude=13.454837;
SET @departureId:=LAST_INSERT_ID();

INSERT INTO Cargo SET contractStation_id=@departureId,
 weightKg=300, volumeM3=1.1, volumeUnits=2,
 cargoType="pesok" ;

INSERT INTO Cargo SET contractStation_id=@departureId,
 weightKg=700, volumeM3=2.1, volumeUnits=3,
 cargoType="holy water" ;

INSERT INTO Cargo SET contractStation_id=@arrivalId,
 weightKg=300, volumeM3=1.1, volumeUnits=2,
 cargoType="pesok" ;

INSERT INTO Cargo SET contractStation_id=@arrivalId,
 weightKg=700, volumeM3=2.1, volumeUnits=3,
 cargoType="holy water" ;

-- --------------------------------------------------------------------------
