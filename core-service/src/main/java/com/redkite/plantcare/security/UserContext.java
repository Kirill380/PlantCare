package com.redkite.plantcare.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

public class UserContext implements Serializable {
  private final String username;
  private final List<GrantedAuthority> authorities;

  private UserContext(String username, List<GrantedAuthority> authorities) {
    this.username = username;
    this.authorities = authorities;
  }

  /**
   * Fabric method for creating new Instance of UserContext.
   */
  public static UserContext create(String username, List<GrantedAuthority> authorities) {
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("Username is blank: " + username);
    }
    return new UserContext(username, authorities);
  }

  public String getUsername() {
    return username;
  }

  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }
}