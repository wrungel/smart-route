package com.smartroute.service;


import com.smartroute.model.Customer;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class CustomerRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Customer> customerEventSrc;

   public void register(Customer customer) throws Exception {
      em.persist(customer);
      customerEventSrc.fire(customer);
   }
}
