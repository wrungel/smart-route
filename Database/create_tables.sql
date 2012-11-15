
CREATE DATABASE IF NOT EXISTS `LkwSchedulerDB` ;
USE `LkwSchedulerDB` ;

CREATE TABLE IF NOT EXISTS Account
(
login VARCHAR(20),
passwd VARCHAR(32),
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Customer
(
 name VARCHAR(150),
 address VARCHAR(300),
 phone VARCHAR(20),
 email VARCHAR(100),
 companyName VARCHAR(150),
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 accountId INT NOT NULL,
 CONSTRAINT fk_customerAccount FOREIGN KEY (accountId) REFERENCES Account(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Driver  -- or Spedition?!
(
 name VARCHAR(150),
 email VARCHAR(100),
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 accountId INT NOT NULL,
 CONSTRAINT fk_driverAccount FOREIGN KEY (accountId) REFERENCES Account(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Truck
(
 driverId INT NOT NULL,                        -- foreign key
 isAvailable BOOL,                             -- whether it is available for scheduling
 vehicleModel VARCHAR(150),
 vehicleIdNummer VARCHAR(10),                  -- kfz-Nummer
 capacityUnits SMALLINT,         				       -- Euro-Paletten
 capacityKg INT,
 capacityM3 DECIMAL(5, 3),    				         -- cubiq meter
 priceKm DECIMAL(5, 2),          					     -- maximal: 999,99 EUR per km :)
 priceHour DECIMAL(5, 2),           			  	 -- means a hour waiting
 mobile VARCHAR(20),             		      		 -- mobile phone
 addressHome VARCHAR(300),      					     -- complete adress of the home-station
 zipHome VARCHAR(10),                   			 -- plz der home-station
 latHome DECIMAL(8, 6),       		   	         -- latitude of the home-station
 longHome DECIMAL(8, 6),        			  	     -- longitude of the home-station
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

 CONSTRAINT fk_driver FOREIGN KEY (driverId) REFERENCES Driver(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Contract -- Auftrag  ..  aka 'Order' but cannot be callled so beacause of 'Order By' sql command
(
 toBeAssigned BOOL,
 sealed BOOL,                                  -- soll Wagen plombiert werden
 price DECIMAL(7, 2),                          -- maximal: 99999,99 EUR :)
 entireWeightKg INT,
 entireVolumeM3 DECIMAL(5, 3),                 -- in qubic meter
 entireVolumeUnits SMALLINT,                   -- Euro-Paletten
 reception DATETIME,
 decayTime DATETIME,                           -- Verfallszeitpunkt des Auftrages
 customersComment TEXT,
 assignmentId INT,                             -- assignments id if assigned to a vehicle, NULL otherwise
 customerId INT,
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

 CONSTRAINT fk_Customer FOREIGN KEY (customerId) REFERENCES Customer(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ContractStation
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

 contractId INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary for the contract
 INDEX(contractId, numberInSequence),

 kind ENUM('load', 'unload', 'driveBy'), -- driveBy not used, reserved for advanced features
 timeFrom DATETIME,
 timeUntil DATETIME,
 address VARCHAR(300),                   -- complete adress
 zip     VARCHAR(10),                    -- Postleitzahl
 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6),                -- geo coordinate

 CONSTRAINT fk_Contract FOREIGN KEY (contractId) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Route
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 truckId INT NOT NULL,                   -- equals the key in corresponding record in the truck-table
 suggestionId INT,                       -- schould be set for a suggested route, may be NUll for a really driven one
 isRealRoute BOOL,                       -- 'true' means truck really drives the route,'false' - route is suggested but not actual truck route
 lastchange TIMESTAMP,

 CONSTRAINT fk_Truck FOREIGN KEY (truckId) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS RouteStation
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 
 routeId INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary
 INDEX(routeId, numberInSequence),

 contractStation INT,                    -- corresponding contract station (may be NULL f.e. if driver communicates he will be at some place on his own)

 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6),                -- geo coordinate

 kind ENUM('load', 'unload', 'driveBy'),
 timeFrom DATETIME,
 timeUntil DATETIME,

 leftKg INT,                             -- kilogramm, capacity remaining
 leftM3 DECIMAL(5, 3),                   -- in qubic meter, capacity remaining
 leftUnits SMALLINT,                     -- paletten, capacity remaining

 CONSTRAINT fk_Route FOREIGN KEY (routeId) REFERENCES Route(id) ON DELETE CASCADE ON UPDATE CASCADE,

 CONSTRAINT fk_contractStation FOREIGN KEY (contractStation) REFERENCES ContractStation(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Cargo
(
 weightKg INT,                           -- kilogramm
 volumeM3 DECIMAL(5, 3),                 -- in qubic meter
 volumeUnits SMALLINT,                   -- Euro-Paletten
 cargoType VARCHAR(50),                  -- TODO: clear requirements, should it be INT or ENUM?
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 routeStationId INT,
 contractStationId INT,

CONSTRAINT fk_routeStationId FOREIGN KEY (routeStationId) REFERENCES RouteStation(id) ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_contractStationId FOREIGN KEY (contractStationId) REFERENCES ContractStation(id) ON DELETE SET NULL ON UPDATE CASCADE

) ENGINE=InnoDB;


-- a suggestion groups multiple TentativeAssignments (for a single truck?) which do not contradict each other
CREATE TABLE IF NOT EXISTS Suggestion
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 schedulerStarted DATETIME,                       -- when the schudeluer was started
 subNumber TINYINT                                -- the number inside the output of a specific scheduler's run
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS TentativeAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 suggId INT NOT NULL,
 orderId INT NOT NULL,
 INDEX(suggId, orderId),         -- a truck can get assigned multiple contracts, but in one Suggestion there can be only one truck for each contract
 vehicleId INT NOT NULL,         -- aka 'truckId'
 status ENUM('suggested', 'smsSent', 'driverDenied', 'driverAccepted', 'dismissed', 'promoted'),
 statusChange TIMESTAMP,

 CONSTRAINT fk_Sugg FOREIGN KEY (suggId) REFERENCES Suggestion(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Order FOREIGN KEY (orderId) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Vehicle FOREIGN KEY (vehicleId) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS FinalAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 orderIdFin INT NOT NULL,
 vehicleIdFin INT NOT NULL,
 tentativeAssignmentId INT,
 status ENUM('new', 'inProgress', 'done'),
 statusChange TIMESTAMP,

 CONSTRAINT fk_OrderFin FOREIGN KEY (orderIdFin) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_VehicleFin FOREIGN KEY (vehicleIdFin) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE,

 CONSTRAINT fk_Tentative FOREIGN KEY (tentativeAssignmentId) REFERENCES TentativeAssignment(id) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB;
