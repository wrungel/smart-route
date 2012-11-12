
CREATE DATABASE IF NOT EXISTS `LkwSchedulerDB` ;
USE `LkwSchedulerDB` ;

CREATE TABLE IF NOT EXISTS Customer
(
 name VARCHAR(150),
 address VARCHAR(300),
 phone VARCHAR(20),
 email VARCHAR(100),
 passwd VARCHAR(10),
 companyName VARCHAR(150),
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Driver  -- or Spedition?!
(
 name VARCHAR(150),
 email VARCHAR(100),
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Truck
(
 driverId INT NOT NULL,           -- foreign key
 isAvailable BOOL,                -- whether it is available for schedulong
 vehicleModel VARCHAR(150),
 vehicleIdNummer VARCHAR(10),     -- kfz-Nummer
 capacityUnits SMALLINT,          -- Euro-Paletten
 capacityKg INT,
 capacityM3 DECIMAL(5, 3),        -- cubiq meter
 priceKm DECIMAL(5, 2),            -- maximal: 999,99 EUR per km :)
 priceHour DECIMAL(5, 2),          -- means a hour waiting
 mobile VARCHAR(20),              -- mobile phone
 addressHome VARCHAR(300),        -- complete adress of the home-station
 zipHome VARCHAR(10),             -- plz der home-station
 latHome DECIMAL(8, 6),            -- latitude of the home-station
 longHome DECIMAL(8, 6),           -- longitude of the home-station
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Contract -- Auftrag
(
 loadType VARCHAR(50),
 toBeAssigned BOOL,
 sealed BOOL,                      -- soll Wagen plombiert werden
 price DECIMAL(7, 2),              -- maximal: 99999,99 EUR :)
 entireWeightKg INT,
 entireVolumeM3 DECIMAL(5, 3),     -- in qubic meter
 entireVolumeUnits SMALLINT,       -- Euro-Paletten
 reception DATETIME,
 decayTime DATETIME,               -- Verfallszeitpunkt des Auftrages
 customersComment TEXT,
 assignmentId INT,                 -- assignments id if assigned to a vehicle, NULL otherwise
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS ContractStation
(
 contractId INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary for the contract
 INDEX(contractId, numberInSequence),

 kind ENUM('load', 'unload', 'driveBy'), -- driveBy not used, reserved for advanced features
 weightKg INT,                           -- kilogramm
 volumeM3 DECIMAL(5, 3),                 -- in qubic meter
 volumeUnits SMALLINT,                   -- Euro-Paletten
 timeFrom DATETIME,
 timeUntil DATETIME,
 routeStationId INT,                     -- corresponding station on a route of truckif exists, NULL otherwise
 address VARCHAR(300),                   -- complete adress
 zip     VARCHAR(10),                    -- Postleitzahl
 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6)                 -- geo coordinate
);

CREATE TABLE IF NOT EXISTS Route
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 truckId INT NOT NULL,                   -- equals the key in corresponding record in the truck-table
 suggestionId INT,
 isRealRoute BOOL,                       -- 'true' means truck really drives the route,'false' - route is suggested but not actual truck route
 lastchange TIMESTAMP
);

CREATE TABLE IF NOT EXISTS RouteStation
(
 routeId INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary
 INDEX(routeId, numberInSequence),

 contractId INT,                         -- corresponding contract (may be NULL)
 contractStationId INT,                  -- corresponding station of a contract (may be NULL f.e. if driver communicates he will be at some place on his own)

 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6),                -- geo coordinate

 kind ENUM('load', 'unload', 'driveBy'),
 timeFrom DATETIME,
 timeUntil DATETIME,

 weightKg INT,                           -- kilogramm, ammount to load/unload
 volumeM3 DECIMAL(5, 3),                 -- in qubic meter, ammount to load/unload
 volumeUnits SMALLINT,                   -- paletten, ammount to load/unload

 leftKg INT,                             -- kilogramm, capacity remaining
 leftM3 DECIMAL(5, 3),                   -- in qubic meter, capacity remaining
 leftUnits SMALLINT                      -- paletten, capacity remaining
);

CREATE TABLE IF NOT EXISTS TentativeAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 suggestionId INT NOT NULL,
 contractId INT NOT NULL,
 INDEX(suggestionId, contractId),         -- a truck can get assigned multiple contracts, but in one Suggestion there can be only one truck for each contract
 truckId INT NOT NULL,
 status ENUM('suggested', 'smsSent', 'driverDenied', 'driverAccepted', 'dismissed', 'promoted'),
 statusChange TIMESTAMP
);

CREATE TABLE IF NOT EXISTS FinalAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 contractId INT NOT NULL,
 truckId INT NOT NULL,
 tentativeAssignmentId INT NOT NULL,
 status ENUM('new', 'inProgress', 'done'),
 statusChange TIMESTAMP
);

-- a suggestion groups multiple TentativeAssignments, which together comprise a solution for a set of contracts, the contracts which were provided to the Scheduler as input
CREATE TABLE IF NOT EXISTS Suggestion
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 schedulerStarted DATETIME,                       -- when the schudeluer was started
 subNumber TINYINT                                -- scheduler generates multiple suggestions for a set of contracts
);
