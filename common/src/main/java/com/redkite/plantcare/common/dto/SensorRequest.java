package com.redkite.plantcare.common.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
public class SensorRequest {

  @NotBlank
  private String name;

  @Min(500)
  private Integer logFrequency;

}
