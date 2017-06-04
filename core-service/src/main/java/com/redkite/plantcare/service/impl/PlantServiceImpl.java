package com.redkite.plantcare.service.impl;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.controllers.filters.PlantFilter;
import com.redkite.plantcare.dao.PlantDao;
import com.redkite.plantcare.service.PlantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantServiceImpl implements PlantService {

  @Autowired
  private PlantDao plantDao;

  @Override
  public PlantResponse createPlant(PlantRequest plantRequest) {
    return null;
  }

  @Override
  public ItemList<PlantResponse> findPlants(PlantFilter plantFilter, Long userId) {
    return null;
  }

  @Override
  public PlantResponse getPlant(Long id) {
    return null;
  }

  @Override
  public String editPlant(Long id, PlantRequest plantRequest) {
    return null;
  }


  @Override
  public void deletePlant(Long id) {

  }


}
