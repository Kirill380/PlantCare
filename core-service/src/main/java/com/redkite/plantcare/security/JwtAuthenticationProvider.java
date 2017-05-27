package com.redkite.plantcare.security;

import com.redkite.plantcare.exceptions.JwtExpiredTokenException;
import com.redkite.plantcare.security.token.tools.JwtTokenParser;
import com.redkite.plantcare.security.token.tools.TokenVerifier;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {


  private final JwtTokenParser jwtTokenParser;

  private final TokenVerifier tokenVerifier;

  @Autowired
  public JwtAuthenticationProvider(JwtTokenParser jwtTokenParser, TokenVerifier tokenVerifier) {
    this.jwtTokenParser = jwtTokenParser;
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String rawAccessToken = (String) authentication.getCredentials();

    Jws<Claims> jwsClaims;
    try {
      jwsClaims = jwtTokenParser.parseClaims(rawAccessToken);
    } catch (JwtExpiredTokenException ex) {
      throw new AuthenticationServiceException(ex.getMessage());
    }

    if (!tokenVerifier.verify(jwsClaims.getBody().getId())) {
      throw new AuthenticationServiceException("Token is invalid");
    }

    String subject = jwsClaims.getBody().getSubject();
    List<String> scopes = jwsClaims.getBody().get("roles", List.class);
    List<GrantedAuthority> authorities = scopes.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());


    return new JwtAuthenticationToken(UserContext.create(subject, authorities), authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
