package com.redkite.plantcare.security;

import com.redkite.plantcare.model.User;
import com.redkite.plantcare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder encoder;

  private final UserService userService;

  @Autowired
  public AjaxAuthenticationProvider(final UserService userService, final PasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.notNull(authentication, "No authentication data provided");

    String userEmail = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user = userService.getUserByEmail(userEmail);

    if (user == null) {
      throw new UsernameNotFoundException("User not found: " + userEmail);
    }

    if (!encoder.matches(password, user.getPasswordHash())) {
      throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
    }

    if (user.getRole() == null) {
      throw new InsufficientAuthenticationException("User has no roles assigned");
    }

    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));


    return new UsernamePasswordAuthenticationToken(UserContext.create(user.getId(), authorities), null, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
