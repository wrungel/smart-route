package com.smartroute.model;

// Generated 17.11.2012 01:35:47 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "driver", catalog = "LkwSchedulerDB")
public class Driver implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(length = 150)	
	private String name;

	@Column(name = "email", length = 100)
	private String email;
	
	@Column(nullable = false)
	@OneToOne
	private Account account;

	public Driver() {
	}

	public Driver(Account account) {
		this.account = account;
	}

	public Driver(String name, String email, Account account) {
		this.name = name;
		this.email = email;
		this.account = account;
	}

	public Long getId() {
		return this.id;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccountId(Account account) {
		this.account = account;
	}

}
