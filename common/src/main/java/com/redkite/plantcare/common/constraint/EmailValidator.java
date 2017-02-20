package com.redkite.plantcare.common.constraint;

import static org.kaaproject.sentinel.dto.utils.ConstraintsConstants.EMAIL_MAX_LENGTH;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<Email, String> {

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
