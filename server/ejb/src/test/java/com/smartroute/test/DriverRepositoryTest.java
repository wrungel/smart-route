package com.smartroute.test;

import com.smartroute.model.Account;
import com.smartroute.model.Driver;
import com.smartroute.model.Truck;
import com.smartroute.service.AccountRepository;
import com.smartroute.service.DriverRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class DriverRepositoryTest extends AbstractPersistenceTest {

    Account account;



    @Before
    public void setUp() {
        super.setUp();
        AccountRepository accountRepository = new AccountRepository(getEntityManager());
        account = new Account();
        account.setEmail("xxx@example.com");
        account.setLogin("testLogin");
        account.setName("testName");
        account.setPasswd("testPassword123");
        accountRepository.persist(account);
    }


    @Test
    public void create_driver_with_two_tracks() {
        Driver driver = new Driver();        
        driver.setAccount(account);
        Truck truck = driver.createTruck();
        truck.setAddressHome("Berlin");
        truck.setCapacityKg(101);
        truck.setCapacityM3(new BigDecimal(13));
        truck.setCapacityUnits(Short.valueOf("" + 22));
        truck.setIsAvailable(true);
        truck.setMobile("+49152018276318");


        truck = driver.createTruck();
        truck.setAddressHome("Aachen");
        truck.setCapacityKg(101);
        truck.setCapacityM3(new BigDecimal(13));
        truck.setCapacityUnits(Short.valueOf("" + 22));
        truck.setIsAvailable(true);
        truck.setMobile("+49152018276318");

        DriverRepository driverRepository = new DriverRepository(getEntityManager());
        driverRepository.persist(driver);

        getEntityManager().clear();
        Driver persistedDriver = driverRepository.findById(driver.getId());
        assertThat(persistedDriver.getTrucks(), hasSize(2));
    }

}
