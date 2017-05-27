package com.redkite.plantcare.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataFilter {

  private LocalDateTime from;

  private LocalDateTime to;

}
