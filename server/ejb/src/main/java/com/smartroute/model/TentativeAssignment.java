package com.smartroute.model;

// Generated 17.11.2012 01:35:47 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TentativeAssignment implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(nullable = false)
	@ManyToOne(optional=false)
	private Suggestion suggestion;

	@Column(nullable = false)
	@ManyToOne(optional=false)
	private Contract contract;

	@Column(nullable = false)
	@ManyToOne(optional=false)
	private Truck truck;

	private TentativeAssignmentStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, length = 19)
	private Date statusChange;

	public TentativeAssignment() {
	}

	public TentativeAssignment(Suggestion suggestion, Contract contract, Truck truck,
			Date statusChange) {
		this.suggestion = suggestion;
		this.contract = contract;
		this.truck = truck;
		this.statusChange = statusChange;
	}

	public TentativeAssignment(Suggestion suggestion, Contract contract, Truck truck,
			TentativeAssignmentStatus status, Date statusChange) {
		this.suggestion = suggestion;
		this.contract = contract;
		this.truck = truck;
		this.status = status;
		this.statusChange = statusChange;
	}

	public Integer getId() {
		return this.id;
	}

	public Suggestion getSuggestion() {
		return this.suggestion;
	}

	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Truck getTruck() {
		return this.truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public TentativeAssignmentStatus getStatus() {
		return this.status;
	}

	public void setStatus(TentativeAssignmentStatus status) {
		this.status = status;
	}

	public Date getStatusChange() {
		return this.statusChange;
	}

	public void setStatusChange(Date statusChange) {
		this.statusChange = statusChange;
	}

}
