package com.redkite.plantcare.common.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<Email, String> {

  public static final int EMAIL_MAX_LENGTH = 100;

  private static final String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
          + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  @Override
  public void initialize(Email constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !(value == null || value.length() > EMAIL_MAX_LENGTH) && value.matches(pattern);
  }
}
