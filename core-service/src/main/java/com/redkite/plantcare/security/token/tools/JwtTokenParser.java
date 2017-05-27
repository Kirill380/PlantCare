package com.redkite.plantcare.security.token.tools;

import com.redkite.plantcare.configurations.JwtSettings;
import com.redkite.plantcare.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenParser {

  private final JwtSettings jwtSettings;

  @Autowired
  public JwtTokenParser(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  /**
   * Parses and validates JWT Token signature.
   */
  public Jws<Claims> parseClaims(String token) throws JwtExpiredTokenException {
    try {
      return Jwts.parser().setSigningKey(jwtSettings.getTokenSigningKey()).parseClaimsJws(token);
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
      log.error("Invalid JWT Token", ex);
      throw new BadCredentialsException("Invalid JWT token: ", ex);
    } catch (ExpiredJwtException expiredEx) {
      log.info("JWT Token is expired", expiredEx);
      throw new JwtExpiredTokenException(token, "JWT Token expired", expiredEx);
    }
  }
}
