package com.smartroute.model;

import com.google.common.collect.ImmutableList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
public class Driver implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@OneToOne(optional=false)
	private Account account;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="driver", orphanRemoval=true)
	private List<Truck> trucks = new ArrayList<>();
	
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
	
	public List<Truck> getTrucks() {
        return ImmutableList.copyOf(trucks);
    }
	
	public Truck createTruck() {
	    Truck truck = new Truck(); 
	    trucks.add(truck);
	    truck.setDriver(this);
	    return truck;
	}
	
	
}
