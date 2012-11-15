package com.smartroute.service;

import com.smartroute.model.Customer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CustomerLoginService {

    @Inject EntityManager em;

    public Customer login(String username, String password) {
        TypedQuery<Customer> query =
                em.createQuery("SELECT c FROM Customer c WHERE c.name = :name", Customer.class);
        query.setParameter("name", username);
        List<Customer> results = query.getResultList();
        if (!results.isEmpty() && results.get(0).getPassword().equals(password))
            return results.get(0);
        else
            return null;
        
    }
}
