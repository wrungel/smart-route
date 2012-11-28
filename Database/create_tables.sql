CREATE TABLE IF NOT EXISTS Account
(
 login VARCHAR(20) UNIQUE,
 passwd VARCHAR(32),
 name VARCHAR(150),
 email VARCHAR(100),
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Customer
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 address VARCHAR(300),
 phone VARCHAR(20),
 companyName VARCHAR(150),
 account_id INT NOT NULL,
 CONSTRAINT fk_Account_Customer FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Driver  -- or Spedition?!
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 account_id INT NOT NULL,
 CONSTRAINT fk_Account_Driver FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Truck
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 driver_id INT NOT NULL,                       -- foreign key
 isAvailable BOOL,                             -- whether it is available for scheduling
 truckModel VARCHAR(150),
 truckIdNummer VARCHAR(10),                    -- kfz-Nummer
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

 CONSTRAINT fk_Driver_Truck FOREIGN KEY (driver_id) REFERENCES Driver(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Contract -- Auftrag  ..  aka 'Order' but cannot be callled so beacause of 'Order By' sql command
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 toBeAssigned BOOL,
 sealed BOOL,                                  -- soll Wagen plombiert werden
 price DECIMAL(7, 2),                          -- maximal: 99999,99 EUR :)
 entireWeightKg INT,
 entireVolumeM3 DECIMAL(5, 3),                 -- in qubic meter
 entireVolumeUnits SMALLINT,                   -- Euro-Paletten
 reception DATETIME,
 decayTime DATETIME,                           -- Verfallszeitpunkt des Auftrages
 customersComment TEXT,
 assignmentId INT,                             -- assignments id if assigned to a truck, NULL otherwise
 customer_id INT NOT NULL,

 CONSTRAINT fk_Customer_Contract FOREIGN KEY (customer_id) REFERENCES Customer(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ContractStation
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

 contract_id INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary for the contract
 INDEX(contract_id, numberInSequence),

 kind ENUM('LOAD', 'UNLOAD', 'DRIVE_BY'),
 timeFrom DATETIME,
 timeUntil DATETIME,

 address VARCHAR(300),                   -- complete adress
 zip     VARCHAR(10),                    -- Postleitzahl
 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6),                -- geo coordinate

 CONSTRAINT fk_Contract_ContractStation FOREIGN KEY (contract_id) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;



-- a suggestion groups multiple TentativeAssignments for a single truck which do not contradict each other
CREATE TABLE IF NOT EXISTS Suggestion
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 schedulerStarted DATETIME,                       -- when the schudeluer was started
 subNumber TINYINT,                               -- the number inside the output of a specific scheduler's run
 truck_id INT,                                     -- one suggestion is for a single truck
 CONSTRAINT fk_Truck_Suggestion FOREIGN KEY (truck_id) REFERENCES Truck(id) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS Route
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 truck_id INT NOT NULL,                   -- equals the key in corresponding record in the truck-table
 suggestion_id INT,                       -- schould be set for a suggested route, may be NUll for a really driven one
 isRealRoute BOOL,                       -- 'true' means truck really drives the route,'false' - route is suggested but not actual truck route
 lastchange TIMESTAMP,

 CONSTRAINT fk_Truck_Route FOREIGN KEY (truck_id) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Suggestion_Route FOREIGN KEY (suggestion_id) REFERENCES Suggestion(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS RouteStation
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

 route_id INT NOT NULL,
 numberInSequence TINYINT NOT NULL,      -- the number inside of the itinerary
 INDEX(route_id, numberInSequence),

 contractStation_id INT,                    -- corresponding contract station (may be NULL f.e. if driver communicates he will be at some place on his own)

 latitude  DECIMAL(8, 6),                -- geo coordinate
 longitude DECIMAL(8, 6),                -- geo coordinate

 kind ENUM('LOAD', 'UNLOAD', 'DRIVE_BY'),
 timeFrom DATETIME,
 timeUntil DATETIME,

 leftKg INT,                             -- kilogramm, capacity remaining
 leftM3 DECIMAL(5, 3),                   -- in qubic meter, capacity remaining
 leftUnits SMALLINT,                     -- paletten, capacity remaining

 CONSTRAINT fk_Route_RouteStation FOREIGN KEY (route_id) REFERENCES Route(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_ContractStation_RouteStation FOREIGN KEY (contractStation_id) REFERENCES ContractStation(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Cargo
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 weightKg INT,                           -- kilogramm
 volumeM3 DECIMAL(5, 3),                 -- in qubic meter
 volumeUnits SMALLINT,                   -- Euro-Paletten
 cargoType VARCHAR(50),                  -- TODO: clear requirements, should it be INT or ENUM?
 routeStation_id INT,
 contractStation_id INT,                 -- at least one of these 2 foreign keys should be NOT NULL, a cargo can belong to a routeStation or to a contractStation, or to both (if route station is copied from contract station)

CONSTRAINT fk_RouteStation_Cargo FOREIGN KEY (routeStation_id) REFERENCES RouteStation(id) ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_ContractStation_Cargo FOREIGN KEY (contractStation_id) REFERENCES ContractStation(id) ON DELETE SET NULL ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS TentativeAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 suggestion_id INT NOT NULL,
 contract_id INT NOT NULL,
 INDEX(suggestion_id, contract_id),         -- a truck can get assigned multiple contracts, but in one Suggestion there can be only one truck for each contract
 truck_id INT NOT NULL,
 status ENUM('SUGGESTED', 'SMS_SENT', 'DRIVER_DENIED', 'DRIVER_ACCEPTED', 'DISMISSED', 'PROMOTED'),
 statusChange TIMESTAMP,

 CONSTRAINT fk_Suggestion_TentativeAssignment FOREIGN KEY (suggestion_id) REFERENCES Suggestion(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Contract_TentativeAssignment FOREIGN KEY (contract_id) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Truck_TentativeAssignment FOREIGN KEY (truck_id) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS FinalAssignment
(
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 contract_id INT NOT NULL,
 truck_id INT NOT NULL,
 tentativeAssignment_id INT,
 status ENUM('NEW', 'IN_PROGRESS', 'DONE'),
 statusChange TIMESTAMP,

 CONSTRAINT fk_Contract_FinalAssignment FOREIGN KEY (contract_id) REFERENCES Contract(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_Truck_FinalAssignment FOREIGN KEY (truck_id) REFERENCES Truck(id) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT fk_TentativeAssignment_FinalAssignment FOREIGN KEY (tentativeAssignment_id) REFERENCES TentativeAssignment(id) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB;
