package com.smartroute.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
public class Driver implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@OneToOne(optional=false)
	private Account account;

	public Driver() {
	}

	public Driver(Account account) {
		this.account = account;
	}

	public Long getId() {
		return this.id;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
