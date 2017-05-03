//package com.redkite.plantcare.filter;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.redkite.plantcare.common.dto.AuthRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.validation.Validator;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    private final AuthenticationSuccessHandler successHandler;
//    private final AuthenticationFailureHandler failureHandler;
//
//    private final ObjectMapper objectMapper;
//
//
//    private final Validator validator;
//
//    public JwtAuthenticationFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
//                                   AuthenticationFailureHandler failureHandler, ObjectMapper mapper, Validator validator) {
//        super(defaultProcessUrl);
//        this.successHandler = successHandler;
//        this.failureHandler = failureHandler;
//        this.objectMapper = mapper;
//        this.validator = validator;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException, IOException, ServletException {
//        if (!HttpMethod.POST.name().equals(request.getMethod())) {
//            if (log.isDebugEnabled()) {
//                log.debug("Authentication method not supported. Request method: " + request.getMethod());
//            }
//            throw new AuthenticationServiceException("Authentication method not supported");
//        }
//
//        AuthRequest authRequest = objectMapper.readValue(request.getReader(), AuthRequest.class);
//
//        if (StringUtils.isBlank(authRequest.getUsername()) || StringUtils.isBlank(authRequest.getPassword())) {
//            throw new AuthenticationServiceException("Username or Password not provided");
//        }
//
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
//
//        return this.getAuthenticationManager().authenticate(`token);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//        successHandler.onAuthenticationSuccess(request, response, authResult);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                              AuthenticationException failed) throws IOException, ServletException {
//        SecurityContextHolder.clearContext();
//        failureHandler.onAuthenticationFailure(request, response, failed);
//    }
//}