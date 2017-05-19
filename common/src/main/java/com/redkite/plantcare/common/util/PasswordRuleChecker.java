package com.redkite.plantcare.common.util;


import lombok.Getter;

public enum PasswordRuleChecker {

  DEFAULT(true, true, true, false), // check all except special characters
  ALL_CHECKS(true, true, true, true);

  public static final String NULL_STRING_VIOLATION = "The password can't be null";
  public static final String LENGTH_VIOLATION = "The password length should be equal or greater that 8";
  public static final String MAX_LENGTH_VIOLATION = "The password length should not be greater that 255";
  public static final String ALLOWED_SYMBOLS_VIOLATION = "The password contains illegal symbols. "
          + "You are only allowed to use upper and lower case latin letters, digits and special symbols _#?!@$%^&*-";
  public static final String LOWER_CASE_VIOLATION = "The password should have at least one "
          + "lower case latin letter";
  public static final String UPPER_CASE_VIOLATION = "The password should have at least one "
          + "upper case latin letter";
  public static final String DIGIT_VIOLATION = "The password  should have at least one upper digit";
  public static final String SPECIAL_CHARACTER_VIOLATION = "The password should have at least one "
          + "special character";
  public static final String SUCCESS = "Success";
  private static final String LOWER_CASE = "a-z";
  private static final String UPPER_CASE = "A-Z";
  private static final String DIGIT = "0-9";
  private static final String SPECIAL_CHARACTER = "_#?!@$%^&*-"; // big set !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
  private static final int MIN_LENGTH = 8;
  private static final int MAX_LENGTH = 255;
  private boolean checkLowerCase;
  private boolean checkUpperCase;
  private boolean checkDigit;
  private boolean checkSpecialCharacter;


  /**
   * Instantiates a password validator with passed properties.
   *
   * @param checkLowerCase        check that password contains at least one lowe case letter
   * @param checkUpperCase        check that password contains at least one upper case letter
   * @param checkDigit            check that password contains at least one digit
   * @param checkSpecialCharacter check that password contains at least one special character
   */
  PasswordRuleChecker(boolean checkLowerCase, boolean checkUpperCase,
                      boolean checkDigit, boolean checkSpecialCharacter) {
    this.checkLowerCase = checkLowerCase;
    this.checkUpperCase = checkUpperCase;
    this.checkDigit = checkDigit;
    this.checkSpecialCharacter = checkSpecialCharacter;
  }


  /**
   * Perform validation on passed password.
   *
   * @param password the password
   */
  public ValidationResult validate(String password) {
    if (password == null) {
      return new ValidationResult(false, NULL_STRING_VIOLATION);
    }

    if (password.length() < MIN_LENGTH) {
      return new ValidationResult(false, LENGTH_VIOLATION);
    }

    if (password.length() > MAX_LENGTH) {
      return new ValidationResult(false, MAX_LENGTH_VIOLATION);
    }

    String allowedSymbols = "[" + UPPER_CASE + LOWER_CASE + DIGIT + SPECIAL_CHARACTER + "]+";

    if (!password.matches(allowedSymbols)) {
      return new ValidationResult(false, ALLOWED_SYMBOLS_VIOLATION);
    }

    if (checkLowerCase && !password.matches("(?=.*?[" + LOWER_CASE + "]).+")) {
      return new ValidationResult(false, LOWER_CASE_VIOLATION);
    }

    if (checkUpperCase && !password.matches("(?=.*?[" + UPPER_CASE + "]).+")) {
      return new ValidationResult(false, UPPER_CASE_VIOLATION);
    }

    if (checkDigit && !password.matches("(?=.*?[" + DIGIT + "]).+")) {
      return new ValidationResult(false, DIGIT_VIOLATION);
    }

    if (checkSpecialCharacter && !password.matches("(?=.*?[" + SPECIAL_CHARACTER + "]).+")) {
      return new ValidationResult(false, SPECIAL_CHARACTER_VIOLATION);
    }

    return new ValidationResult(true, SUCCESS);

  }


  @Getter
  public static class ValidationResult {

    private boolean isValid;
    private String message;

    public ValidationResult(boolean isValid, String message) {
      this.isValid = isValid;
      this.message = message;
    }
  }
}
