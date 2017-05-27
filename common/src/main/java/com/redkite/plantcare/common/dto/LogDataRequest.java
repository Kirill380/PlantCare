package com.redkite.plantcare.common.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
public class LogDataRequest {

  @NotBlank
  private String endpointId;

  private String dataType;

  @NotBlank
  private Integer value;

  private LocalDateTime logTime;
}
