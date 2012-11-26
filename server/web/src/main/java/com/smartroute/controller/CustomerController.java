package com.smartroute.controller;

import com.smartroute.service.ContractService;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.smartroute.model.Account;
import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class CustomerController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private CustomerRegistration customerRegistration;

    @Inject
    private ContractService orderService;
    
    private Customer newCustomer;

    private Account newAccount;
    
    @Produces
    @Named
    public Account getNewAccount() {
		return newAccount;
	}
    
    @Produces
    @Named
    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void register() throws Exception {
    	
        customerRegistration.register(newAccount, newCustomer);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
        initNewCustomer();
    }

    @PostConstruct
    public void initNewCustomer() {
        newCustomer = new Customer();
        newAccount = new Account();
    }
}
