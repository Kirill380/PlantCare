package com.redkite.plantcare.controllers;


import com.redkite.plantcare.common.dto.NotificationRequest;
import com.redkite.plantcare.notifications.NotificationException;
import com.redkite.plantcare.notifications.senders.NotificationSender;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

  @Autowired
  private NotificationSender notificationSender;

  @RequestMapping(value = "/email", method = RequestMethod.POST)
  public void sendEmailNotification(@RequestBody NotificationRequest notificationRequest) {
    notificationSender.sendNotification(notificationRequest);
  }


  @RequestMapping(value = "/viber", method = RequestMethod.POST)
  public void sendViberNotification(@RequestBody NotificationRequest notificationRequest) {
    throw new UnsupportedOperationException();
  }


  @RequestMapping(value = "/twitter", method = RequestMethod.POST)
  public void sendTwitterNotification(@RequestBody NotificationRequest notificationRequest) {
    throw new UnsupportedOperationException();
  }


  /**
   * Handles exceptions and returns an appropriate HTTP code.
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handle(HttpServletResponse response, Throwable ex) {
    log.debug("Caught an exception:", ex);
    if (ex instanceof NotificationException) {
      NotificationException notificationException = (NotificationException) ex;
      return new ResponseEntity<>(notificationException.getMessage(), notificationException.getStatus());
    } else {
      log.error("Unexpected exception is caught:", ex);
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
