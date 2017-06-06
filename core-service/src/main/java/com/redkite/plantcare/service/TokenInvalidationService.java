package com.redkite.plantcare.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenInvalidationService {

  void invalidateAllTokensForUser(Long userId);

  void invalidateToken(String tokenId);

  boolean isTokenInvalidated(Jws<Claims> tokenClaims);
}
