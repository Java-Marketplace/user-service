package com.jmp.userservice.validation.annotation;

import com.jmp.userservice.validation.validator.LinksValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LinksValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLinks {
    String message() default "Links are not allowed or invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
