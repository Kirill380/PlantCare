package com.redkite.plantcare.security.token.tools;

import com.redkite.plantcare.service.TokenInvalidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlackListTokenVerifier implements TokenVerifier {

  @Autowired
  private TokenInvalidationService tokenInvalidationService;

  @Override
  public boolean verify(Jws<Claims> tokenClaims) {
    return !tokenInvalidationService.isTokenInvalidated(tokenClaims);
  }
}
