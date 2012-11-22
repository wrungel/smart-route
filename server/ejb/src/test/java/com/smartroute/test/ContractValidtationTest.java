package com.smartroute.test;

import com.google.common.base.Joiner;
import com.smartroute.model.Contract;
import com.smartroute.model.ContractStation;
import com.smartroute.model.ContractStationKind;
import com.smartroute.model.Customer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class ContractValidtationTest {

    static Logger log = LoggerFactory.getLogger(ContractValidtationTest.class.getName());
    static ValidatorFactory validatorFactory;
    Validator validator;

    @BeforeClass
    public static void beforeClass() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @Before
    public void setUp() {
        validator = validatorFactory.getValidator();
    }

    @Test
    @Ignore
    public void load_unload_is_balanced() {
        Contract contract = new Contract();

        Customer customer = Mockito.mock(Customer.class);
        contract.setCustomer(customer);

        createStation(contract, "Amsterdam", ContractStationKind.LOAD);
        createStation(contract, "Hamburg", ContractStationKind.LOAD);
        createStation(contract, "Berlin", ContractStationKind.UNLOAD);
        
        Set<ConstraintViolation<Contract>> violations = validator.validate(contract, Default.class);
        
        log.info(Joiner.on(", ").join(violations));
        
        assertThat("the sum of load stations is bigger than the sum of unload stations causes constraint violation", 
                violations, not(empty()));
    }

    

    ContractStation createStation(Contract contract, String address, ContractStationKind kind) {
        ContractStation station = contract.createStation();
        station.setAddress(address);
        station.setKind(kind);
        return station;
    }

}
