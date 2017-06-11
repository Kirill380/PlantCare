package com.redkite.plantcare.mocksensor;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class LogDataRequest {

  private Long sensorId;

  private String dataType;

  private Integer value;

  private LocalDateTime logTime;
}
