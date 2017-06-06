package com.redkite.plantcare.security.token.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

//@Component
public class BloomFilterTokenVerifier implements TokenVerifier {

  @Override
  public boolean verify(Jws<Claims> tokenClaims) {
    return true;
  }
}