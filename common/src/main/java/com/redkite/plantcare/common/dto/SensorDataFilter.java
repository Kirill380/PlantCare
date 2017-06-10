package com.redkite.plantcare.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataFilter {

  @NotNull
  private Long sensorId;

  private LocalDateTime from;

  private LocalDateTime to;

}
