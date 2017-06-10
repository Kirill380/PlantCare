package com.redkite.plantcare.common.dto;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
public class SensorRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String wifiName;

  @NotBlank
  private String wifiPassword;

  @NotBlank
  private String dataType;

  @Min(500)
  private Integer logFrequency;

}
