package com.smartroute.test;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRegistration;
import com.smartroute.util.Resources;

@RunWith(Arquillian.class)
public class CustomerRegistrationIT {

	@Inject org.slf4j.Logger log;

	public static final String DEFAULT_PERSISTENCE_XML_PATH = "META-INF/test-persistence.xml";

	@Deployment
	public static Archive<?> createTestArchive() {


		
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(Customer.class, CustomerRegistration.class, Resources.class)
				.addAsResource(System.getProperty("persistenceXML", "META-INF/persistence.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource(System.getProperty("dataSource", "test-ds.xml"), "test-ds.xml");
	}

	@Inject
	CustomerRegistration customerRegistration;

	@Test
	public void testRegister() throws Exception {

		Customer newCustomer = new Customer();
		newCustomer.setName("Jane Doe");
		newCustomer.setEmail("jane@mailinator.com");
		newCustomer.setPhoneNumber("2125551234");
		customerRegistration.register(newCustomer);
		assertNotNull(newCustomer.getId());
		log.info(newCustomer.getName() + " was persisted with id " + newCustomer.getId());
	}

}
