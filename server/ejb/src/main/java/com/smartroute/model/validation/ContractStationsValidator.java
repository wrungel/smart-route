package com.smartroute.model.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.smartroute.model.ContractStation;

/**
 * This is just a cheap validator prototype  
 * @author frol
 *
 */
public class ContractStationsValidator implements ConstraintValidator<ContractStationsValidatorDefault, List<ContractStation>> {

    @Override
    public void initialize(ContractStationsValidatorDefault constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<ContractStation> stations, ConstraintValidatorContext ctx) {
    	return true;
    }
    
    
}
