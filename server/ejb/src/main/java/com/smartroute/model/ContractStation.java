package com.smartroute.model;

import org.hibernate.validator.util.Contracts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ContractStation {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(optional=false)
    private Order contract;
    
    private ConstractStationKind kind;
    private BigDecimal weightKg;
    private BigDecimal volumeM3;
    private Integer volumeUnits;
    
    private Date timeFrom;
    private Date timeUntil;
    
    // corresponding station on a route of truckif exists, NULL otherwise
    // private Long routeStationId;
    
    
    /**
     * the number inside of the itinerary for the contract
     */
    private Integer numberInSequence;
    
    @Size(max=300)
    // complete adress
    private String address;
    
    @Size(max=10)
    private String zip;
    
    // geo coordinate
    // DECIMAL(8, 6)
    private BigDecimal latitude;
    
    // geo coordinate
    // DECIMAL(8, 6)
    private BigDecimal longitude;
    
    
    
    
    public Long getId() {
        return id;
    }
    

    
    public Order getContract() {
        return contract;
    }
    
    public void setContract(Order contract) {
        this.contract = contract;
    }
    
    
    public Integer getNumberInSequence() {
        return numberInSequence;
    }
    
    
    public void setNumberInSequence(Integer numberInSequence) {
        this.numberInSequence = numberInSequence;
    }
        
    public ConstractStationKind getKind() {
        return kind;
    }
    
    public void setKind(ConstractStationKind kind) {
        this.kind = kind;
    }
    
    
    public BigDecimal getWeightKg() {
        return weightKg;
    }
    
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }
    
    
    public BigDecimal getVolumeM3() {
        return volumeM3;
    }
    
    public void setVolumeM3(BigDecimal volumeM3) {
        this.volumeM3 = volumeM3;
    }
    
    
    public Integer getVolumeUnits() {
        return volumeUnits;
    }
    
    public void setVolumeUnits(Integer volumeUnits) {
        this.volumeUnits = volumeUnits;
    }
    
    
    public Date getTimeFrom() {
        return timeFrom;
    }
    
    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }
    
    
    public void setTimeUntil(Date timeUntil) {
        this.timeUntil = timeUntil;
    }
    
    
    public Date getTimeUntil() {
        return timeUntil;
    }
    
    public String getAddress() {
        return address;
    }
    
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public String getZip() {
        return zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
}
