package com.redkite.plantcare.common.constraint;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, String> {

  private String[] allowedValues;

  @Override
  public void initialize(AllowedValues constraintAnnotation) {
    allowedValues = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || Arrays.stream(allowedValues).anyMatch(s -> s.equals(value));
  }
}
