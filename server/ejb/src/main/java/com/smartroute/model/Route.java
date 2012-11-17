package com.smartroute.model;

// Generated 17.11.2012 01:35:47 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Route implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(name = "truckId", nullable = false)
	@OneToOne	
	private Truck truck;

	@Column(name = "suggestionId")
	private Suggestion suggestion;
	
	private Boolean isRealRoute;
	private Date lastchange;

	public Route() {
	}

	public Route(Truck truck, Date lastchange) {
		this.truck = truck;
		this.lastchange = lastchange;
	}

	public Route(Truck truck, Suggestion suggestion, Boolean isRealRoute,
			Date lastchange) {
		this.truck = truck;
		this.suggestion = suggestion;
		this.isRealRoute = isRealRoute;
		this.lastchange = lastchange;
	}

	public Long getId() {
		return this.id;
	}


	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public Suggestion getSuggestion() {
		return this.suggestion;
	}

	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}

	public Boolean getIsRealRoute() {
		return this.isRealRoute;
	}

	public void setIsRealRoute(Boolean isRealRoute) {
		this.isRealRoute = isRealRoute;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastchange", nullable = false, length = 19)
	public Date getLastchange() {
		return this.lastchange;
	}

	public void setLastchange(Date lastchange) {
		this.lastchange = lastchange;
	}

}
