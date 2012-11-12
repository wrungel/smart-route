package com.smartroute.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

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
@Table(name="Contract")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Size(max=50)
    private String loadType;
    
    private boolean toBeAssigned;
    
    /**
     * soll Wagen plombiert werden?
     */
    private boolean sealed;
    
    // TODO: use JScience
    private BigDecimal entireWeightKg;
    
    // TODO: use JScience
    private BigDecimal price;
    
    // TODO: use JScience
    private Integer entireVolumeUnits;
    
    // TODO: use JScience
    private BigDecimal entireVolumeM3;
    /**
     * Auftragseingang
     */
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime reception;
    
    /**
     * Verfallszeitpunkt des Auftrages
     */
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime decayTime;
    
    @Size(max=500)
    private String customersComment;
    
    
    public Long getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "id: " + id;
    }
    
    
    public BigDecimal getPrice() {
        return price;
    }
    
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
    public String getLoadType() {
        return loadType;
    }
    
    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }
    
}
