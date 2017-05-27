package com.redkite.plantcare.security.token.tools;


public interface TokenVerifier {

  boolean verify(String jti);
}
