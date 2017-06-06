package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.nosql.BlackListedToken;

public interface BlackListedTokenDao {

  BlackListedToken addTokenToBlackList(String accessToken);

  boolean isTokenInBlackList(String accessToken);
}
