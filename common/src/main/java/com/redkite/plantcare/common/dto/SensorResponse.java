package com.redkite.plantcare.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorResponse {

  private Long id;

  private String name;

  private String status;

  private String dataType;

  private Integer logFrequency;

  private LocalDateTime creationDate;
}
