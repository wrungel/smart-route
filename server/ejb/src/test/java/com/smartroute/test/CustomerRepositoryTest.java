package com.smartroute.test;

import org.junit.Ignore;

import com.smartroute.model.Account;
import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRepository;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class CustomerRepositoryTest extends AbstractPersistenceTest {

    private static final String LOGIN = "testLogin";


    @Before
    public void setUp() {
        super.setUp();
        
        Account account = new Account(LOGIN, "testPassword", "testName");
        getEntityManager().persist(account);

        Customer customer = new Customer(account);

        customer.setAddress("asaa");
        customer.setCompanyName("asdfkjah");
        customer.setAccount(account);
    }


    @Test
    @Ignore
    public void customer_is_found_if_login_is_correct () {
        CustomerRepository  customerRepository = new CustomerRepository(getEntityManager());
        Customer customer = customerRepository.findCustomerByLogin(LOGIN);
        assertThat(customer, notNullValue(Customer.class));
    }

    @Test
    public void customer_is_not_found_if_login_is_not_correct () {
        CustomerRepository  customerRepository = new CustomerRepository(getEntityManager());
        Customer customer = customerRepository.findCustomerByLogin(LOGIN + "123");
        assertThat(customer, nullValue(Customer.class));
    }

}
