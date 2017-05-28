package com.redkite.plantcare.common.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
public class PlantRequest {

  @NotBlank
  private String name;

  private String image; // base64 image

  private String species;

  private String location;

  @Min(1)
  private Integer moistureThreshold;
}
