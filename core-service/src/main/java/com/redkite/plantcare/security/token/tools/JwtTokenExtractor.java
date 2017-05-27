package com.redkite.plantcare.security.token.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenExtractor {

  private static String HEADER_PREFIX = "Bearer ";

  /**
   *  Extract raw token by remove <b>Bearer</b> prefix.
   */
  public String extract(String header) {
    if (StringUtils.isBlank(header)) {
      throw new AuthenticationServiceException("Authorization header cannot be blank!");
    }

    if (header.length() < HEADER_PREFIX.length()) {
      throw new AuthenticationServiceException("Invalid authorization header size.");
    }

    return header.substring(HEADER_PREFIX.length(), header.length());
  }
}
