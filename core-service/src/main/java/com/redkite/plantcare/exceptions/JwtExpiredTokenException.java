package com.redkite.plantcare.exceptions;

import javax.naming.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {
  private static final long serialVersionUID = -5959543783324224864L;

  private String token;

  public JwtExpiredTokenException(String msg) {
    super(msg);
  }

  public JwtExpiredTokenException(String token, String msg, Throwable throwable) {
    super(msg);
    this.token = token;
  }

  public String token() {
    return this.token;
  }
}
