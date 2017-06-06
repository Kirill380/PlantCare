package com.redkite.plantcare.dao.impl;

import com.redkite.plantcare.dao.BlackListedTokenDao;
import com.redkite.plantcare.model.nosql.BlackListedToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class BlackListedTokenDaoImpl extends AbstractCassandraDao<BlackListedToken, String> implements BlackListedTokenDao {

  //TODO refactor diff ttl for refresh and access tokens
  @Value("${security.jwt.refreshExpTime}")
  private Integer refreshExpTime;

  @Override
  public BlackListedToken addTokenToBlackList(String accessToken) {
    return save(new BlackListedToken(accessToken), (int) TimeUnit.MINUTES.toSeconds(refreshExpTime));
  }

  @Override
  public boolean isTokenInBlackList(String accessToken) {
    return find(accessToken) != null;
  }

  @Override
  protected Class<BlackListedToken> getColumnFamilyClass() {
    return BlackListedToken.class;
  }

  @Override
  protected String getColumnFamilyName() {
    return "token_blacklist";
  }
}
