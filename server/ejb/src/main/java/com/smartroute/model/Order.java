package com.smartroute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.math.BigDecimal;



/**
 *     CREATE TABLE IF NOT EXISTS Contract -- Auftrag
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

 * @author frol
 *
 */
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    // TODO: use JScience
    private BigDecimal entireWeightKg;
    
    // TODO: use JScience
    private BigDecimal price;
    
    private Integer entireVolumeUnits;
    
    
    
    
}
