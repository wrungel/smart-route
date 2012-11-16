package com.smartroute.model.validation;

import com.smartroute.model.ContractStation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * This is just a cheap validator prototype: checks only the weight balance over all contract stations 
 * TODO: while checking weight balance consider time intervals and order of station  
 * @author frol
 *
 */
public class ContractStationsValidator implements ConstraintValidator<ContractStationsValidatorDefault, List<ContractStation>> {

    @Override
    public void initialize(ContractStationsValidatorDefault constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<ContractStation> stations, ConstraintValidatorContext ctx) {
        BigDecimal totalWeigh = BigDecimal.ZERO;
        for (ContractStation station: stations) {
            switch (station.getKind())
            {
            case load: totalWeigh = totalWeigh.add(station.getWeightKg()); break;
            case unload: totalWeigh = totalWeigh.subtract(station.getWeightKg()); break;
            case driveBy: break;
            }
        }
        boolean balanced = totalWeigh.equals(BigDecimal.ZERO);
        return balanced;
        
    }
    
    
}
