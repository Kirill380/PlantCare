package com.redkite.plantcare.service;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.PlantFilter;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PlantService {

  @PreAuthorize("hasAuthority('regularUser')")
  PlantResponse createPlant(Long userId, PlantRequest plantRequest);

  @PreAuthorize("hasAuthority('regularUser')")
  ItemList<PlantResponse> findPlants(PlantFilter plantFilter, Long userId);

  @PreAuthorize("hasAuthority('regularUser')")
  PlantResponse getPlant(Long userId, Long id);

  @PreAuthorize("hasAuthority('regularUser')")
  void editPlant(Long userId, Long id, PlantRequest plantRequest);

  @PreAuthorize("hasAuthority('regularUser')")
  void deletePlant(Long userId, Long id);

  List<SensorResponse> getSensorsByPlant(Long plantId);
}
