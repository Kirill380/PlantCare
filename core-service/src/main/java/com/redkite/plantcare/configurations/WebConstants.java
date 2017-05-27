package com.redkite.plantcare.configurations;


public final class WebConstants {

  private WebConstants() {
  }

  public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";
  public static final String LOGIN_ENTRY_POINT = "/auth/login";
  public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
  public static final String REFRESH_TOKEN_ENTRY_POINT = "/auth/token";
  public static final String LOGOUT_ENTRY_POINT = "/auth/logout";
}
