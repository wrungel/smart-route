package com.smartroute.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Truck implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(optional=false)
	private Driver driver;

	private Boolean isAvailable;
	
	@Column(length = 150)
	private String truckModel;
	
	@Column(length = 10)	
	private String truckIdNummer;

	private Short capacityUnits;
	
	private Integer capacityKg;
	
	@Column(precision = 5, scale = 3)	
	private BigDecimal capacityM3;
	
	@Column(precision = 5)	
	private BigDecimal priceKm;
	
	@Column(precision = 5)	
	private BigDecimal priceHour;
	
	@Column(length = 20)
	private String mobile;
	
	@Column(length = 300)	
	private String addressHome;
	
	@Column(length = 10)	
	private String zipHome;
	
	@Column(precision = 8, scale = 6)	
	private BigDecimal latHome;
	
	@Column(precision = 8, scale = 6)	
	private BigDecimal longHome;

	public Truck() {
	}

	public Truck(Driver driver) {
		this.driver = driver;
	}

	public Truck(Driver driver, Boolean isAvailable, String truckModel,
			String truckIdNummer, Short capacityUnits, Integer capacityKg,
			BigDecimal capacityM3, BigDecimal priceKm, BigDecimal priceHour,
			String mobile, String addressHome, String zipHome,
			BigDecimal latHome, BigDecimal longHome) {
		this.driver = driver;
		this.isAvailable = isAvailable;		
		this.truckModel = truckModel;
		this.truckIdNummer = truckIdNummer;
		this.capacityUnits = capacityUnits;
		this.capacityKg = capacityKg;
		this.capacityM3 = capacityM3;
		this.priceKm = priceKm;
		this.priceHour = priceHour;
		this.mobile = mobile;
		this.addressHome = addressHome;
		this.zipHome = zipHome;
		this.latHome = latHome;
		this.longHome = longHome;
	}

	public Integer getId() {
		return this.id;
	}

	public Driver getDriverId() {
		return this.driver;
	}

	public void setDriverId(Driver driver) {
		this.driver = driver;
	}

	public Boolean getIsAvailable() {
		return this.isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public String getTruckModel() {
		return this.truckModel;
	}

	public void setTruckModel(String truckModel) {
		this.truckModel = truckModel;
	}

	public String getTruckIdNummer() {
		return this.truckIdNummer;
	}

	public void setTruckIdNummer(String truckIdNummer) {
		this.truckIdNummer = truckIdNummer;
	}

	public Short getCapacityUnits() {
		return this.capacityUnits;
	}

	public void setCapacityUnits(Short capacityUnits) {
		this.capacityUnits = capacityUnits;
	}

	public Integer getCapacityKg() {
		return this.capacityKg;
	}

	public void setCapacityKg(Integer capacityKg) {
		this.capacityKg = capacityKg;
	}

	public BigDecimal getCapacityM3() {
		return this.capacityM3;
	}

	public void setCapacityM3(BigDecimal capacityM3) {
		this.capacityM3 = capacityM3;
	}

	public BigDecimal getPriceKm() {
		return this.priceKm;
	}

	public void setPriceKm(BigDecimal priceKm) {
		this.priceKm = priceKm;
	}

	public BigDecimal getPriceHour() {
		return this.priceHour;
	}

	public void setPriceHour(BigDecimal priceHour) {
		this.priceHour = priceHour;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddressHome() {
		return this.addressHome;
	}

	public void setAddressHome(String addressHome) {
		this.addressHome = addressHome;
	}

	public String getZipHome() {
		return this.zipHome;
	}

	public void setZipHome(String zipHome) {
		this.zipHome = zipHome;
	}

	public BigDecimal getLatHome() {
		return this.latHome;
	}

	public void setLatHome(BigDecimal latHome) {
		this.latHome = latHome;
	}

	public BigDecimal getLongHome() {
		return this.longHome;
	}

	public void setLongHome(BigDecimal longHome) {
		this.longHome = longHome;
	}

}
