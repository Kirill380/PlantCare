package com.redkite.plantcare.common.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


//CHECKSTYLE:OFF

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AllowedValuesValidator.class)
@Documented
public @interface AllowedValues {

  String message() default "value is not in range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] value();

}

//CHECKSTYLE:ON