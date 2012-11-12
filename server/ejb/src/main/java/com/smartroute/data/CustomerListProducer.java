package com.smartroute.data;

import com.google.common.collect.Iterables;
import com.smartroute.model.Customer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

@RequestScoped
public class CustomerListProducer {

	private @Inject Logger logger;

	@Inject
	private EntityManager em;

	private List<Customer> customers;

	// @Named provides access the return value via the EL variable name "customers" in the UI (e.g.,
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Customer> getCustomers() {
		return customers;
	}

	public void onCustomerListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Customer customer) {
		retrieveAllCustomersOrderedByName();
	}

	@PostConstruct
	public void retrieveAllCustomersOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
		Root<Customer> customer = criteria.from(Customer.class);
		// Swap criteria statements if you would like to try out type-safe criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(customer).orderBy(cb.asc(customer.get(Customer_.name)));
		criteria.select(customer).orderBy(cb.asc(customer.get("name")));

		customers = em.createQuery(criteria).getResultList();
//		logger.info(on(", ").join(transform(customers, toStringFunction())));
		logger.info(Iterables.toString(customers));
	}
}
