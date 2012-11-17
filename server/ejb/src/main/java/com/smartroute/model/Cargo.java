package com.smartroute.model;

// Generated 17.11.2012 01:35:47 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Cargo implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	private Integer weightKg;
	
	@Column(precision = 5, scale = 3)	
	private BigDecimal volumeM3;
	
	
	private Short volumeUnits;
	
	@Column(name = "cargoType", length = 50)	
	private String cargoType;
	
	
	private Integer routeStationId;
	private Integer contractStationId;

	public Cargo() {
	}

	public Cargo(Integer weightKg, BigDecimal volumeM3, Short volumeUnits,
			String cargoType, Integer routeStationId, Integer contractStationId) {
		this.weightKg = weightKg;
		this.volumeM3 = volumeM3;
		this.volumeUnits = volumeUnits;
		this.cargoType = cargoType;
		this.routeStationId = routeStationId;
		this.contractStationId = contractStationId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWeightKg() {
		return this.weightKg;
	}

	public void setWeightKg(Integer weightKg) {
		this.weightKg = weightKg;
	}

	public BigDecimal getVolumeM3() {
		return this.volumeM3;
	}

	public void setVolumeM3(BigDecimal volumeM3) {
		this.volumeM3 = volumeM3;
	}

	public Short getVolumeUnits() {
		return this.volumeUnits;
	}

	public void setVolumeUnits(Short volumeUnits) {
		this.volumeUnits = volumeUnits;
	}

	public String getCargoType() {
		return this.cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public Integer getRouteStationId() {
		return this.routeStationId;
	}

	public void setRouteStationId(Integer routeStationId) {
		this.routeStationId = routeStationId;
	}

	public Integer getContractStationId() {
		return this.contractStationId;
	}

	public void setContractStationId(Integer contractStationId) {
		this.contractStationId = contractStationId;
	}

}
