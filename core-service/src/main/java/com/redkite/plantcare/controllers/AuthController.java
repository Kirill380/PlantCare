package com.redkite.plantcare.controllers;

import static com.redkite.plantcare.configurations.WebConstants.JWT_TOKEN_HEADER_PARAM;

import com.redkite.plantcare.common.dto.TokensDto;
import com.redkite.plantcare.exceptions.JwtExpiredTokenException;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.security.token.tools.AccessJwtToken;
import com.redkite.plantcare.security.token.tools.JwtTokenExtractor;
import com.redkite.plantcare.security.token.tools.JwtTokenFactory;
import com.redkite.plantcare.security.token.tools.JwtTokenParser;
import com.redkite.plantcare.security.token.tools.TokenVerifier;
import com.redkite.plantcare.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private TokenVerifier tokenVerifier;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenFactory tokenFactory;

  @Autowired
  private JwtTokenParser tokenParser;

  @Autowired
  private JwtTokenExtractor tokenExtractor;


  @RequestMapping(value = "/token", method = RequestMethod.GET)
  public AccessJwtToken refreshToken(@RequestHeader(JWT_TOKEN_HEADER_PARAM) String token) throws JwtExpiredTokenException {
    Jws<Claims> refreshToken = tokenParser.parseClaims(tokenExtractor.extract(token));

    if (refreshToken == null || refreshToken.getBody() == null) {
      throw new AuthenticationServiceException("Token is invalid");
    }

    if (!(boolean) refreshToken.getBody().get("isRefresh")) {
      throw new AuthenticationServiceException("It is not refresh token");
    }

    String jti = refreshToken.getBody().getId();
    if (!tokenVerifier.verify(jti)) {
      throw new AuthenticationServiceException("Token is invalid");
    }

    String subject = refreshToken.getBody().getSubject();
    User user = userService.getUserByEmail(subject);

    if (user.getRole() == null) {
      throw new InsufficientAuthenticationException("User has no roles assigned");
    }

    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));

    UserContext userContext = UserContext.create(user.getEmail(), authorities);

    return tokenFactory.createAccessJwtToken(userContext);
  }

  @RequestMapping(value = "/token", method = RequestMethod.POST)
  public void invalidateToken(@RequestBody TokensDto tokensDto) {

  }

}
