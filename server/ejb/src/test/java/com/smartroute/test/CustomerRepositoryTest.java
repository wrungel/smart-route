package com.smartroute.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.smartroute.model.Account;
import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRepository;

public class CustomerRepositoryTest {

    @Inject org.slf4j.Logger log;

    EntityManager em;

    private static final String LOGIN = "testLogin";

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("test");
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        Account account = new Account(LOGIN, "testPassword", "testName");
        em.persist(account);

        Customer customer = new Customer(account);

        customer.setAddress("asaa");
        customer.setCompanyName("asdfkjah");


        customer.setAccount(account);

        em.persist(customer);
        em.flush();
    }

    @After
    public void cleanUp() {
        em.getTransaction().rollback();
    }

    @Test
    public void customer_is_found_if_login_is_correct () {
        CustomerRepository  customerRepository = new CustomerRepository(em);
        Customer customer = customerRepository.findCustomerByLogin(LOGIN);
        assertThat(customer, notNullValue(Customer.class));
    }

    @Test
    public void customer_is_not_found_if_login_is_not_correct () {
        CustomerRepository  customerRepository = new CustomerRepository(em);
        Customer customer = customerRepository.findCustomerByLogin(LOGIN + "123");
        assertThat(customer, nullValue(Customer.class));
    }

}
