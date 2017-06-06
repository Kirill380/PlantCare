package com.redkite.plantcare.service.impl;


import com.redkite.plantcare.dao.BlackListedTokenDao;
import com.redkite.plantcare.dao.InvalidatedTokensDao;
import com.redkite.plantcare.service.TokenInvalidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenInvalidationServiceImpl implements TokenInvalidationService {

  @Autowired
  private InvalidatedTokensDao invalidatedTokensDao;

  @Autowired
  private BlackListedTokenDao blackListedTokenDao;


  @Value("${security.jwt.expTime}")
  private Integer expTime;

  @Value("${security.jwt.refreshExpTime}")
  private Integer refreshExpTime;


  @Override
  public void invalidateAllTokensForUser(Long userId) {
    long invalidateExpirationTimestamp = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expTime + refreshExpTime);
    invalidatedTokensDao.setTokensInvalidationTimestampForUser(userId, invalidateExpirationTimestamp);
  }

  @Override
  public void invalidateToken(String tokenId) {
    blackListedTokenDao.addTokenToBlackList(tokenId);
  }

  @Override
  public boolean isTokenInvalidated(Jws<Claims> tokenClaims)  {
    Long invalidateExpirationTimestamp = invalidatedTokensDao.getTokensInvalidationTimestampForUser(Long.parseLong(tokenClaims.getBody().getSubject()));
    return (invalidateExpirationTimestamp != null && tokenClaims.getBody().getExpiration().getTime() <= invalidateExpirationTimestamp)
            || blackListedTokenDao.isTokenInBlackList(tokenClaims.getBody().getId());
  }

}
