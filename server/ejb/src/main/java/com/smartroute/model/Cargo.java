package com.smartroute.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

	@ManyToOne
	private RouteStation routeStation;
	
	@ManyToOne
	private ContractStation contractStation;

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

	public RouteStation getRouteStationId() {
		return this.routeStation;
	}

	public void setRouteStationId(RouteStation routeStation) {
		this.routeStation = routeStation;
	}

	public ContractStation getContractStation() {
		return this.contractStation;
	}

	public void setContractStation(ContractStation contractStation) {
		this.contractStation = contractStation;
	}

}
