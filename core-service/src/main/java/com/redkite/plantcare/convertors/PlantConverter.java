package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.model.Plant;


import org.springframework.stereotype.Component;

import java.util.Base64;


@Component
public class PlantConverter implements ToModelConverter<Plant, PlantRequest>, ToDtoConverter<Plant, PlantResponse> {

  @Override
  public Plant toModel(PlantRequest dto) {
    byte[] imageBytes = Base64.getDecoder().decode(dto.getImage());
    Plant plant = new Plant();
    plant.setName(dto.getName());
    plant.setImage(imageBytes);
    plant.setLocation(dto.getLocation());
    plant.setSpecies(dto.getSpecies());
    plant.setMoistureThreshold(dto.getMoistureThreshold());
    return plant;
  }

  @Override
  public PlantResponse toDto(Plant model) {
    String encodedImage = Base64.getEncoder().encodeToString(model.getImage());
    PlantResponse plantResponse = new PlantResponse();
    plantResponse.setId(model.getId());
    plantResponse.setName(model.getName());
    plantResponse.setImage(encodedImage);
    plantResponse.setLocation(model.getLocation());
    plantResponse.setSpecies(model.getSpecies());
    plantResponse.setMoistureThreshold(model.getMoistureThreshold());
    plantResponse.setCreationDate(model.getCreationDate());
    return plantResponse;
  }

}
