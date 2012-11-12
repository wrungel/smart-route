package com.smartroute.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.smartroute.model.Customer;

@Stateless
public class CustomerRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Customer> customerEventSrc;

   public void register(Customer customer) throws Exception {
      log.info("Registering " + customer.getName());
      em.persist(customer);
      customerEventSrc.fire(customer);
   }
}
