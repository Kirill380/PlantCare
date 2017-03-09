package com.redkite.plantcare;


import lombok.Getter;

import org.springframework.http.HttpStatus;

public class PlantCareException extends RuntimeException {

  @Getter
  private HttpStatus status;

  public PlantCareException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

}
