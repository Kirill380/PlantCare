package com.redkite.plantcare.security.token.tools;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * The interface Token verifier.
 */
public interface TokenVerifier {

  /**
   * Return true if token valid otherwise false.
   *
   */
  boolean verify(Jws<Claims> tokenClaims);
}
