package com.redkite.plantcare.notifications;


import org.springframework.http.HttpStatus;

public class NotificationException extends RuntimeException {

  private static final long serialVersionUID = 8364303945614117114L;

  private HttpStatus status;

  public NotificationException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
