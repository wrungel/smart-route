DELETE FROM Contract;                -- first empty tables
DELETE FROM ContractStation;

INSERT INTO Contract SET toBeAssigned=true, loadType="pesok", sealed=true,
   price=999.99,
   entireWeightKg=1000, entireVolumeM3=3.2, entireVolumeUnits=0,
   reception=NOW(),
   decayTime='2013-05-01 12:00:00';
SET @contrId:=LAST_INSERT_ID();

INSERT INTO ContractStation SET contract_id=@contrId, numberInSequence=2, kind="unload",
   weightKg=1000, volumeM3=3.2, volumeUnits=0,
   timeFrom='2013-05-03 12:00:00', timeUntil='2013-05-03 18:00:00',
   address="Heinrich-Könn-Str. 265 40625 Düsseldorf", zip="40265", latitude=51.241622, longitude=6.85277;

INSERT INTO ContractStation SET contract_id=@contrId, numberInSequence=1, kind="load",
   weightKg=1000, volumeM3=3.2, volumeUnits=0,
   timeFrom='2013-05-02 12:00:00', timeUntil='2013-05-02 18:00:00', address="Elsenstr.105 12435 Berlin",
   zip="12435", latitude=52.489653, longitude=13.454837;

-- --------------------------------------------------------------------------
