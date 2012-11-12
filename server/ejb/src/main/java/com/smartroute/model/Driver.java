package com.smartroute.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "Driver", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Driver {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String name;

	@NotNull
	@NotEmpty
	@Email
	private String email;	

	private boolean isAvailable;

	@Size(max=150)
	@NotEmpty
	private String vehicleModel;

	@DecimalMin("1")
	private Short capacityUnits;

	@DecimalMin("1")
	private Integer capacityKg;

	@Column(length = 5, precision = 3)
	@Digits(fraction = 3, integer = 2)
	private BigInteger capacityM3; 

	@Column(length = 5, precision = 2)
	@Digits(fraction = 2, integer = 3)		
	private BigInteger priceKm;

	@Column(length = 5, precision = 2)
	@Digits(fraction = 2, integer = 3)	
	private BigInteger priceHour;

	@NotNull
	@Size(min = 10, max = 12)
	@Digits(fraction = 0, integer = 12)
	private String mobilePhone;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public String getVehicleModel() {
		return vehicleModel;
	}


	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Short getCapacityUnits() {
		return capacityUnits;
	}

	public void setCapacityUnits(Short capacityUnits) {
		this.capacityUnits = capacityUnits;
	}


	public Integer getCapacityKg() {
		return capacityKg;
	}

	public void setCapacityKg(Integer capacityKg) {
		this.capacityKg = capacityKg;
	}


	public BigInteger getCapacityM3() {
		return capacityM3;
	}


	public void setCapacityM3(BigInteger capacityM3) {
		this.capacityM3 = capacityM3;
	}


	public BigInteger getPriceKm() {
		return priceKm;
	}


	public void setPriceKm(BigInteger priceKm) {
		this.priceKm = priceKm;
	}

	public BigInteger getPriceHour() {
		return priceHour;
	}

	public void setPriceHour(BigInteger priceHour) {
		this.priceHour = priceHour;
	}
}
