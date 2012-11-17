package com.smartroute.model;

// Generated 17.11.2012 01:35:47 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Suggestion implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date schedulerStarted;
	
	private Integer subNumber;

	public Suggestion() {
	}

	public Suggestion(Date schedulerStarted, Integer subNumber) {
		this.schedulerStarted = schedulerStarted;
		this.subNumber = subNumber;
	}

	public Integer getId() {
		return this.id;
	}


	public Date getSchedulerStarted() {
		return this.schedulerStarted;
	}

	public void setSchedulerStarted(Date schedulerStarted) {
		this.schedulerStarted = schedulerStarted;
	}

	public Integer getSubNumber() {
		return this.subNumber;
	}

	public void setSubNumber(Integer subNumber) {
		this.subNumber = subNumber;
	}

}
