package com.redkite.plantcare.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class NotificationRequest {

  @NotNull
  private String targetEmail;

  private Map<String, String> emailProperties;

  @NotNull
  private NotificationType type;

}
