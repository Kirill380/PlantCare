package com.redkite.plantcare.configurations;

import static com.redkite.plantcare.configurations.WebConstants.LOGIN_ENTRY_POINT;
import static com.redkite.plantcare.configurations.WebConstants.REFRESH_TOKEN_ENTRY_POINT;
import static com.redkite.plantcare.configurations.WebConstants.TOKEN_BASED_AUTH_ENTRY_POINT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redkite.plantcare.filter.AjaxLoginProcessingFilter;
import com.redkite.plantcare.filter.JwtTokenAuthenticationProcessingFilter;
import com.redkite.plantcare.filter.SkipPathRequestMatcher;
import com.redkite.plantcare.security.AjaxAuthenticationProvider;
import com.redkite.plantcare.security.JwtAuthenticationProvider;
import com.redkite.plantcare.security.RestAuthenticationEntryPoint;
import com.redkite.plantcare.security.token.tools.JwtTokenExtractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import javax.validation.Validator;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private RestAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private AuthenticationSuccessHandler successHandler;

  @Autowired
  private AuthenticationFailureHandler failureHandler;

  @Autowired
  private AjaxAuthenticationProvider ajaxAuthenticationProvider;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  private JwtTokenExtractor jwtTokenExtractor;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Validator validator;

  protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
    AjaxLoginProcessingFilter filter =
            new AjaxLoginProcessingFilter(LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper, validator);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
    List<String> pathsToSkip = Arrays.asList(REFRESH_TOKEN_ENTRY_POINT, LOGIN_ENTRY_POINT);
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
    JwtTokenAuthenticationProcessingFilter filter
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, jwtTokenExtractor, matcher);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(ajaxAuthenticationProvider);
    auth.authenticationProvider(jwtAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable() // We don't need CSRF for JWT based authentication
            .exceptionHandling()
            .authenticationEntryPoint(this.authenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(LOGIN_ENTRY_POINT).permitAll() // Login end-point
            .antMatchers(REFRESH_TOKEN_ENTRY_POINT).permitAll() // Token refresh end-point
            .and()
            .authorizeRequests()
            .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
            .and()
            .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
