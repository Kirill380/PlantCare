package com.redkite.plantcare.service;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.controllers.filters.PlantFilter;

public interface PlantService {

  PlantResponse createPlant(PlantRequest plantRequest);

  ItemList<PlantResponse> findPlants(PlantFilter plantFilter, Long userId);

  PlantResponse getPlant(Long id);

  String editPlant(Long id, PlantRequest plantRequest);

  void deletePlant(Long id);
}
