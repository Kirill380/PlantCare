package com.redkite.plantcare.filter;

import lombok.Getter;

import org.springframework.http.HttpMethod;


public class RestApi {

  @Getter
  private String path;

  private HttpMethod method;

  private RestApi(String path, HttpMethod method) {
    this.path = path;
    this.method = method;
  }

  public String getMethod() {
    return method != null ? method.name() : null;
  }

  //CHECKSTYLE:OFF
  public static RestApi a(String path, HttpMethod method) {
    return new RestApi(path, method);
  }

  public static RestApi a(String path) {
    return new RestApi(path, null);
  }
  //CHECKSTYLE:ON
}
