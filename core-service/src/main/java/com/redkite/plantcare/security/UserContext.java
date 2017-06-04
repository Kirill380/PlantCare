package com.redkite.plantcare.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

public class UserContext implements Serializable {

  private final Long userId;
  private final List<GrantedAuthority> authorities;

  private UserContext(Long userId, List<GrantedAuthority> authorities) {
    this.userId = userId;
    this.authorities = authorities;
  }

  /**
   * Fabric method for creating new Instance of UserContext.
   */
  public static UserContext create(Long userId, List<GrantedAuthority> authorities) {
    return new UserContext(userId, authorities);
  }

  public Long getUserId() {
    return userId;
  }

  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }
}