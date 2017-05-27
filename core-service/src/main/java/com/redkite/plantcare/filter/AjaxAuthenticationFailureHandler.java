package com.redkite.plantcare.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redkite.plantcare.common.dto.ErrorDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO replace with ExceptionHandlerController
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper mapper;

  @Autowired
  public AjaxAuthenticationFailureHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException ex) throws IOException, ServletException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    mapper.writeValue(response.getWriter(), new ErrorDto(ex.getMessage(), null));
  }
}