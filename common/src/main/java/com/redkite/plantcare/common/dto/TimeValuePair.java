package com.redkite.plantcare.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeValuePair {

  private Long timestamp;

  private Integer value;
}
