package com.redkite.plantcare.security.token.tools;

import com.redkite.plantcare.configurations.JwtSettings;
import com.redkite.plantcare.security.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

  public static final String IS_REFRESH = "isRefresh";

  private final JwtSettings settings;

  @Autowired
  public JwtTokenFactory(JwtSettings settings) {
    this.settings = settings;
  }

  /**
   * Factory method for issuing new JWT Tokens.
   */
  public AccessJwtToken createAccessJwtToken(UserContext userContext) {
    if (userContext.getUserId() == null) {
      throw new IllegalArgumentException("Cannot create JWT Token without user id");
    }

    if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) {
      throw new IllegalArgumentException("User doesn't have any privileges");
    }

    Claims claims = Jwts.claims().setSubject(userContext.getUserId().toString());
    claims.put("roles", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

    DateTime currentTime = new DateTime();

    String token = Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuer(settings.getTokenIssuer())
            .setIssuedAt(currentTime.toDate())
            .setExpiration(currentTime.plusMinutes(settings.getExpTime()).toDate())
            .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
            .compact();

    return new AccessJwtToken(token, claims);
  }

  /**
   * Factory method for issuing new refresh tokens.
   */
  public AccessJwtToken createRefreshToken(UserContext userContext) {
    if (userContext.getUserId() == null) {
      throw new IllegalArgumentException("Cannot create JWT Token without user id");
    }

    DateTime currentTime = new DateTime();

    Claims claims = Jwts.claims().setSubject(userContext.getUserId().toString());
    claims.put(IS_REFRESH, true);

    String token = Jwts.builder()
            .setClaims(claims)
            .setIssuer(settings.getTokenIssuer())
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(currentTime.toDate())
            .setExpiration(currentTime.plusMinutes(settings.getRefreshExpTime()).toDate())
            .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
            .compact();

    return new AccessJwtToken(token, claims);
  }
}