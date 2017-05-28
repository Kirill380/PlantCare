package com.redkite.plantcare.common.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlantResponse {

  private Long id;

  private String image;

  private String name;

  private String species;

  private String location;

  private Integer moistureThreshold;

  private LocalDateTime creationDate;

}
