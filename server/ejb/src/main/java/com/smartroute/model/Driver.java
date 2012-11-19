package com.smartroute.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;


@Entity
public class Driver implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(length = 150)	
	private String name;

	@Column(length = 100)
	private String email;
	
	@OneToOne(optional=false)
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

	@Size(min=2, max=150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email
	@Size(max=100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
