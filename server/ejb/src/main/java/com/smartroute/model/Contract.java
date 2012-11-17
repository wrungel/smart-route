package com.smartroute.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;



/**
 *  @author frol
 *
 */
@Entity
public class Contract implements Serializable {
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
    @NotNull
    private BigDecimal entireWeightKg = BigDecimal.ZERO;
    
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
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER, mappedBy="contract")
    @Valid
    private List<ContractStation> stations = new ArrayList<>();
    
    @ManyToOne(optional=false)
    private Customer customer;
    
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
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
    
    
    public List<ContractStation> getStations() {
        return stations;
    }
    
    public boolean isToBeAssigned() {
        return toBeAssigned;
    }
    
    public void setToBeAssigned(boolean toBeAssigned) {
        this.toBeAssigned = toBeAssigned;
    }
    
    public boolean isSealed() {
        return sealed;
    }
    
    public void setSealed(boolean sealed) {
        this.sealed = sealed;
    }
    
    public Integer getEntireVolumeUnits() {
        return entireVolumeUnits;
    }
    
    public void setEntireVolumeUnits(Integer entireVolumeUnits) {
        this.entireVolumeUnits = entireVolumeUnits;
    }
    
    public BigDecimal getEntireWeightKg() {
        return entireWeightKg;
    }
    
    public void setEntireWeightKg(BigDecimal entireWeightKg) {
        this.entireWeightKg = entireWeightKg;
    }
    
    public BigDecimal getEntireVolumeM3() {
        return entireVolumeM3;
    }
    
    public void setEntireVolumeM3(BigDecimal entireVolumeM3) {
        this.entireVolumeM3 = entireVolumeM3;
    }
    
    
    public String getCustomersComment() {
        return customersComment;
    }
    
    public void setCustomersComment(String customersComment) {
        this.customersComment = customersComment;
    }
    
   
    
    /**
     * ContractStation factory method: created ContractStation is automatically added to Contract
     * @return
     */
    public ContractStation createStation() {
       ContractStation station = new ContractStation();
       station.setContract(this);
       this.getStations().add(station);
       return station;
    }
}
