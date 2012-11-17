package com.smartroute.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class FinalAssignment implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	@OneToOne
	private Contract contract;
	
	@Column(nullable = false)
	@ManyToOne(optional=false)
	private Truck truck;

	private TentativeAssignment tentativeAssignment;

	@Column(length = 10)
	private FinalAssignmentStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "statusChange", nullable = false, length = 19)	
	private Date statusChange;

	public FinalAssignment() {
	}

	public FinalAssignment(Contract contract, Truck truck, Date statusChange) {
		this.contract = contract;
		this.truck = truck;
		this.statusChange = statusChange;
	}

	public FinalAssignment(Contract contract, Truck truck,
			TentativeAssignment tentativeAssignmentId, FinalAssignmentStatus status, Date statusChange) {
		this.contract = contract;
		this.truck = truck;
		this.tentativeAssignment = tentativeAssignmentId;
		this.status = status;
		this.statusChange = statusChange;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Truck getTruk() {
		return this.truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public TentativeAssignment getTentativeAssignment() {
		return this.tentativeAssignment;
	}

	public void setTentativeAssignmentId(TentativeAssignment tentativeAssignment) {
		this.tentativeAssignment = tentativeAssignment;
	}

	public FinalAssignmentStatus getStatus() {
		return this.status;
	}

	public void setStatus(FinalAssignmentStatus status) {
		this.status = status;
	}

	public Date getStatusChange() {
		return this.statusChange;
	}

	public void setStatusChange(Date statusChange) {
		this.statusChange = statusChange;
	}

}
