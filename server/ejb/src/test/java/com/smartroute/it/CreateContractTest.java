package com.smartroute.it;

import com.smartroute.model.Account;
import com.smartroute.model.Cargo;
import com.smartroute.model.Contract;
import com.smartroute.model.ContractStation;
import com.smartroute.model.ContractStationKind;
import com.smartroute.model.Customer;
import com.smartroute.model.Driver;
import com.smartroute.model.FinalAssignment;
import com.smartroute.model.FinalAssignmentStatus;
import com.smartroute.model.Route;
import com.smartroute.model.RouteStation;
import com.smartroute.model.StationKind;
import com.smartroute.model.Suggestion;
import com.smartroute.model.TentativeAssignment;
import com.smartroute.model.TentativeAssignmentStatus;
import com.smartroute.model.Truck;
import com.smartroute.service.AccountRepository;
import com.smartroute.service.ContractDispatcher;
import com.smartroute.service.ContractService;
import com.smartroute.service.CustomerRegistration;
import com.smartroute.util.Resources;
import com.smartroute.util.SchedulerApi;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.io.File;
import java.math.BigDecimal;

@RunWith(Arquillian.class)
public class CreateContractTest {

    @Inject org.slf4j.Logger log;


    
    @Deployment
    public static Archive<?> createTestArchive() {
        MavenDependencyResolver resolver = DependencyResolvers.use(
                MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
             
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addManifest()
//                .addAsResource(new File("D:\\tmp\\smart-route\\Scheduler\\lib\\target\\smart-route-scheduler.dll"))
                .addClasses(
                        Account.class,
                        Cargo.class,
                        Contract.class,
                        ContractStation.class,
                        ContractStationKind.class,
                        Customer.class,
                        Driver.class,
                        FinalAssignment.class,
                        FinalAssignmentStatus.class,                        
                        Route.class,
                        RouteStation.class,
                        StationKind.class,
                        Suggestion.class,
                        TentativeAssignment.class,
                        TentativeAssignmentStatus.class,
                        Truck.class,
                        ContractService.class, 
                        ContractDispatcher.class,
                        AccountRepository.class,
                        CustomerRegistration.class,
                        Resources.class,
                        SchedulerApi.class
                        )
                .addAsResource("META-INF/persistence-mysql.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                        resolver.artifact("org.jadira.usertype:usertype.jodatime").resolveAsFiles())
                .addAsLibraries(
                        resolver.artifact("joda-time:joda-time").resolveAsFiles());
        // Deploy our test datasource
        //.addAsWebInfResource("ds-mysql.xml");
    }

    @Inject ContractService contractService;
    @Inject AccountRepository accountRepository;
    @Inject CustomerRegistration customerRegistration;

    @Test
    public void testRegister() throws Exception {
        Contract contract = new Contract();
        contract.setEntireWeightKg(BigDecimal.TEN);
        
        
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
        contract.setCustomer(newCustomer);
        
        contractService.create(contract);
        log.info("Customer was persisted with id " + contract.getId());
    }

}
