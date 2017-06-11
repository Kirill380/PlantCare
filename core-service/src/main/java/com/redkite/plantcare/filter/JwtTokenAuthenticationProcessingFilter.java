package com.redkite.plantcare.filter;


import com.redkite.plantcare.configurations.WebConstants;
import com.redkite.plantcare.security.JwtAuthenticationToken;
import com.redkite.plantcare.security.token.tools.JwtTokenExtractor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private final AuthenticationFailureHandler failureHandler;

  private final JwtTokenExtractor jwtTokenExtractor;

  @Autowired
  public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
                                                JwtTokenExtractor jwtTokenExtractor, RequestMatcher matcher) {
    super(matcher);
    this.failureHandler = failureHandler;
    this.jwtTokenExtractor = jwtTokenExtractor;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
          throws AuthenticationException, IOException, ServletException {
    String tokenPayload = request.getHeader(WebConstants.JWT_TOKEN_HEADER_PARAM);
    String token = jwtTokenExtractor.extract(tokenPayload);
    return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}
