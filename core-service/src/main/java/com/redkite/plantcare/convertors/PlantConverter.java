package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.model.Plant;


import org.springframework.stereotype.Component;


@Component
public class PlantConverter implements ToModelConverter<Plant, PlantRequest>, ToDtoConverter<Plant, PlantResponse> {

  @Override
  public Plant toModel(PlantRequest dto) {
    Plant plant = new Plant();
    plant.setName(dto.getName());
    plant.setImage(dto.getImage());
    plant.setLocation(dto.getLocation());
    plant.setSpecies(dto.getSpecies());
    plant.setMoistureThreshold(dto.getMoistureThreshold());
    return plant;
  }

  @Override
  public PlantResponse toDto(Plant model) {
    PlantResponse plantResponse = new PlantResponse();
    plantResponse.setId(model.getId());
    plantResponse.setName(model.getName());
    plantResponse.setImage(model.getImage());
    plantResponse.setLocation(model.getLocation());
    plantResponse.setSpecies(model.getSpecies());
    plantResponse.setMoistureThreshold(model.getMoistureThreshold());
    plantResponse.setCreationDate(model.getCreationDate());
    return plantResponse;
  }

}
