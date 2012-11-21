package com.smartroute.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@Entity
@XmlRootElement
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Size(min = 10, max = 24)
    @Digits(fraction = 0, integer = 24)
    @Column(length = 20)
    private String phone;

    @Size(max = 300)
    private String address;
    
    @Size(max = 150)
    private String companyName;

    @OneToOne(optional=false)
    private Account account;
    
    public Long getId() {
        return id;
    }
    
    public Customer() {
    }
    
    public Customer(Account account) {
        this.account = account;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    public String getCompanyName() {
		return companyName;
	}
    
    public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
    public String getAddress() {
		return address;
	}
    
    public void setAddress(String address) {
		this.address = address;
	}
    
    public Account getAccount() {
		return account;
	}
    
    public void setAccount(Account account) {
		this.account = account;
	}

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}