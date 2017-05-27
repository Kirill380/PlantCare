package com.redkite.plantcare.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.security.token.tools.AccessJwtToken;
import com.redkite.plantcare.security.token.tools.JwtTokenFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper mapper;

  private final JwtTokenFactory tokenFactory;

  @Autowired
  public AjaxAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
    this.mapper = mapper;
    this.tokenFactory = tokenFactory;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    UserContext userContext = (UserContext) authentication.getPrincipal();

    AccessJwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
    AccessJwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("token", accessToken.getToken());
    tokenMap.put("refreshToken", refreshToken.getToken());

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    mapper.writeValue(response.getWriter(), tokenMap);

    clearAuthenticationAttributes(request);
  }

  /**
   * Removes temporary authentication-related data which may have been stored
   * in the session during the authentication process..
   */
  protected final void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}