package com.redkite.plantcare.dao.impl;

import com.redkite.plantcare.dao.InvalidatedTokensDao;
import com.redkite.plantcare.model.nosql.InvalidatedTokens;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class InvalidatedTokensDaoImpl extends AbstractCassandraDao<InvalidatedTokens, Long> implements InvalidatedTokensDao {

  @Value("${security.jwt.expTime}")
  private Integer expTime;

  @Value("${security.jwt.refreshExpTime}")
  private Integer refreshExpTime;

  @Override
  public Long getTokensInvalidationTimestampForUser(Long userId) {
    InvalidatedTokens res = find(userId);
    return res != null ? res.getTimestamp() : null;
  }

  @Override
  public InvalidatedTokens setTokensInvalidationTimestampForUser(Long userId, Long timestamp) {
    return save(new InvalidatedTokens(userId, timestamp), (int) TimeUnit.MINUTES.toSeconds(expTime + refreshExpTime));
  }

  @Override
  protected Class<InvalidatedTokens> getColumnFamilyClass() {
    return InvalidatedTokens.class;
  }

  @Override
  protected String getColumnFamilyName() {
    return "invalidated_tokens";
  }
}
