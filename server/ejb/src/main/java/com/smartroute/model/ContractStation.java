package com.smartroute.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ContractStation implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
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
}
