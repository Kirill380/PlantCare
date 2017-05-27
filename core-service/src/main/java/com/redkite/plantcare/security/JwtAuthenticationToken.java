package com.redkite.plantcare.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 2877954820905567501L;

  private String rawAccessToken;

  private UserContext userContext;

  //CHECKSTYLE:OFF
  public JwtAuthenticationToken(String rawAccessToken) {
    super(null);
    this.rawAccessToken = rawAccessToken;
    this.setAuthenticated(false);
  }

  public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.eraseCredentials();
    this.userContext = userContext;
    super.setAuthenticated(true);
  }
  //CHECKSTYLE:ON

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException(
              "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }
    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return rawAccessToken;
  }

  @Override
  public Object getPrincipal() {
    return this.userContext;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.rawAccessToken = null;
  }
}