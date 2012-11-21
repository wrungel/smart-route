package com.smartroute.model;

import org.hibernate.annotations.Index;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@org.hibernate.annotations.Table(appliesTo = "ContractStation",
indexes={
        @Index(name="contract_sequence", columnNames = {"contract_id", "numberInSequence"})
})
public class ContractStation implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne(optional=false)
    private Contract contract;

    @Column(nullable = false)	
    private Integer numberInSequence;

    private ContractStationKind kind;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFrom;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUntil;

    @Column(length = 300)	
    private String address;

    @Column(length = 10)	
    private String zip;

    @Column(precision = 8, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 8, scale = 6)	
    private BigDecimal longitude;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="contractStation")
    private List<Cargo> cargos = new ArrayList<>(); 

    public Long getId() {
        return this.id;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getNumberInSequence() {
        return this.numberInSequence;
    }

    public void setNumberInSequence(Integer numberInSequence) {
        this.numberInSequence = numberInSequence;
    }

    public ContractStationKind getKind() {
        return this.kind;
    }

    public void setKind(ContractStationKind kind) {
        this.kind = kind;
    }

    public Date getTimeFrom() {
        return this.timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeUntil() {
        return this.timeUntil;
    }

    public void setTimeUntil(Date timeUntil) {
        this.timeUntil = timeUntil;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public boolean isLoad() {
        return kind == ContractStationKind.LOAD;
    }

    public void setLoad(boolean isLoad) {
        if (isLoad)
            kind = ContractStationKind.LOAD;
        else
            kind = ContractStationKind.UNLOAD;
    }
    
    
    public List<Cargo> getCargos() {
        return cargos;
    }
    
    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }
}
