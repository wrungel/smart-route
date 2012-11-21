package com.smartroute.it;

import com.smartroute.service.AccountRepository;

import com.smartroute.model.Account;

import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRegistration;
import com.smartroute.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class CustomerRegistrationIT {

    @Inject org.slf4j.Logger log;

    @Deployment
    public static Archive<?> createTestArchive() {

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AccountRepository.class, Customer.class, CustomerRegistration.class, Resources.class, Account.class)
                .addAsResource("META-INF/persistence-mysql.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        // Deploy our test datasource
        //.addAsWebInfResource("ds-mysql.xml");
    }

    @Inject
    CustomerRegistration customerRegistration;

    @Inject AccountRepository accountRepository;

    @Test
    public void testRegister() throws Exception {
        Account account = new Account();
        account.setName("frol");
        account.setLogin("frol");
        account.setPasswd("frol123");
        accountRepository.persist(account);
        Customer newCustomer = new Customer();
        newCustomer.setAccount(account);
        newCustomer.setAddress("Amsterdam");
        newCustomer.setPhone("+78129283988");
        customerRegistration.register(newCustomer);
        assertNotNull(newCustomer.getId());
        log.info("Customer was persisted with id " + newCustomer.getId());
    }

}
