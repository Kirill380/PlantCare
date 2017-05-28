package com.redkite.plantcare.common.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

@Data
public class LogDataRequest {

  @NotNull
  private Long sensorId;

  private String dataType;

  @NotBlank
  private Integer value;

  private LocalDateTime logTime;
}
