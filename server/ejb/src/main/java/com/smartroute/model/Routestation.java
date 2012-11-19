package com.smartroute.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class RouteStation implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(optional=false)
	private Route route;
	
	@Column(nullable = false)	
	private int numberInSequence;
	private Integer contractStation;
	
	@Column(precision = 8, scale = 6)	
	private BigDecimal latitude;
	
	@Column(name = "longitude", precision = 8, scale = 6)	
	private BigDecimal longitude;
	
	private StationKind kind;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeFrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeUntil;

	private Integer leftKg;
	
	@Column(precision = 5, scale = 3)	
	private BigDecimal leftM3;
	
	private Short leftUnits;

	public RouteStation() {
	}

	public RouteStation(Route route, byte numberInSequence) {
		this.route = route;
		this.numberInSequence = numberInSequence;
	}

	public RouteStation(Route route, int numberInSequence,
			Integer contractStation, BigDecimal latitude, BigDecimal longitude,
			StationKind kind, Date timeFrom, Date timeUntil, Integer leftKg,
			BigDecimal leftM3, Short leftUnits) {
		this.route = route;
		this.numberInSequence = numberInSequence;
		this.contractStation = contractStation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.kind = kind;
		this.timeFrom = timeFrom;
		this.timeUntil = timeUntil;
		this.leftKg = leftKg;
		this.leftM3 = leftM3;
		this.leftUnits = leftUnits;
	}

	public Integer getId() {
		return this.id;
	}


	public Route getRoute() {
		return this.route;
	}

	public void setRouteId(Route route) {
		this.route = route;
	}

	public int getNumberInSequence() {
		return this.numberInSequence;
	}

	public void setNumberInSequence(byte numberInSequence) {
		this.numberInSequence = numberInSequence;
	}

	public Integer getContractStation() {
		return this.contractStation;
	}

	public void setContractStation(Integer contractStation) {
		this.contractStation = contractStation;
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

	public StationKind getKind() {
		return this.kind;
	}

	public void setKind(StationKind kind) {
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

	public Integer getLeftKg() {
		return this.leftKg;
	}

	public void setLeftKg(Integer leftKg) {
		this.leftKg = leftKg;
	}

	public BigDecimal getLeftM3() {
		return this.leftM3;
	}

	public void setLeftM3(BigDecimal leftM3) {
		this.leftM3 = leftM3;
	}

	public Short getLeftUnits() {
		return this.leftUnits;
	}

	public void setLeftUnits(Short leftUnits) {
		this.leftUnits = leftUnits;
	}

}
