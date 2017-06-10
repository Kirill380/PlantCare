package com.redkite.plantcare.common.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
  USER_ACTIVATION("activate"),
  THRESHOLD_EXCEEDED("threshold");

  private final String name;

  NotificationType(String name) {
    this.name = name;
  }

  @JsonValue
  @Override
  public String toString() {
    return name;
  }

  /**
   * Convert String into NotificationType.
   */
  @JsonCreator
  public NotificationType fromString(String str) {
    for (NotificationType type : NotificationType.values()) {
      if (type.name.equals(str)) {
        return type;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

}
