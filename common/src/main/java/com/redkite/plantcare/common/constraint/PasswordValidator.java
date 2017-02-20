package com.redkite.plantcare.common.constraint;


import org.kaaproject.sentinel.dto.utils.PasswordRuleChecker;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private PasswordRuleChecker passwordRuleChecker;

  @Override
  public void initialize(Password constraintAnnotation) {
    passwordRuleChecker = PasswordRuleChecker.DEFAULT;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    PasswordRuleChecker.ValidationResult result = passwordRuleChecker.validate(value);
    if (!result.isValid()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(result.getMessage()).addConstraintViolation();
    }
    return result.isValid();
  }
}
