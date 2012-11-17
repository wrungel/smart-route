package com.smartroute.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.TYPE,
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.FIELD
    
})
@Constraint(validatedBy = ContractStationsValidator.class)
@Documented
public @interface ContractStationsValidatorDefault {
    String  message() default "Constract station invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

