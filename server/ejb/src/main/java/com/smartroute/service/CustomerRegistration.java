package com.smartroute.service;


import com.smartroute.model.Account;
import com.smartroute.model.Customer;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class CustomerRegistration {

	@Inject AccountRepository accountRepository;
	@Inject CustomerRepository customerRepository;

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Customer> customerEventSrc;

	public void register(Account account, Customer customer) throws Exception {
		accountRepository.persist(account);		
		customer.setAccount(account);
		customerRepository.persist(customer);
		customerEventSrc.fire(customer);
	}
}
