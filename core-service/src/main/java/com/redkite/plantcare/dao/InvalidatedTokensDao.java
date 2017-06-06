package com.redkite.plantcare.dao;

import com.redkite.plantcare.model.nosql.InvalidatedTokens;

public interface InvalidatedTokensDao {

  Long getTokensInvalidationTimestampForUser(Long userId);

  InvalidatedTokens setTokensInvalidationTimestampForUser(Long userId, Long timestamp);
}
