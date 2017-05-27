package com.redkite.plantcare.filter;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

public class SkipPathRequestMatcher implements RequestMatcher {

  private OrRequestMatcher matchers;

  private RequestMatcher processingMatcher;

  public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
    Assert.notNull(pathsToSkip);
    List<RequestMatcher> matchers = pathsToSkip.stream()
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toList());
    this.matchers = new OrRequestMatcher(matchers);
    processingMatcher = new AntPathRequestMatcher(processingPath);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    if (matchers.matches(request)) {
      return false;
    }
    return processingMatcher.matches(request);
  }
}