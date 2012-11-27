USE `LkwSchedulerDBTest` ;

DROP TABLE IF EXISTS FinalAssignment ;
DROP TABLE IF EXISTS TentativeAssignment ;
DROP TABLE IF EXISTS Cargo;
DROP TABLE IF EXISTS RouteStation ;
DROP TABLE IF EXISTS Route ;
DROP TABLE IF EXISTS Suggestion ;
DROP TABLE IF EXISTS ContractStation ;
DROP TABLE IF EXISTS Contract ;
DROP TABLE IF EXISTS Truck ;
DROP TABLE IF EXISTS Driver ;
DROP TABLE IF EXISTS Customer ;
DROP TABLE IF EXISTS Account ;

CREATE DATABASE IF NOT EXISTS `LkwSchedulerDBTest` ;
USE `LkwSchedulerDBTest` ;
source create_tables.sql ;