package com.smartroute.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.smartroute.model.Customer;

public class CustomerRepository implements Serializable {
	
	private final EntityManager em;
	
	@Inject
	public CustomerRepository(EntityManager em) {
		this.em = em;
	}
	
	public Customer findCustomerByLogin(String login) {
		TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c INNER JOIN c.account a WHERE a.login = :login", Customer.class);
        query.setParameter("login", login);
        Customer customer = null;
        try {
        	customer = query.getSingleResult();
        } catch (NoResultException nre) {
        	
        }
        return customer;
	}
	
	public void persist(Customer customer) {
		em.persist(customer);
	}
	
	
}
