package com.redkite.plantcare.service.impl;


import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.PlantFilter;
import com.redkite.plantcare.convertors.PlantConverter;
import com.redkite.plantcare.dao.PlantDao;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.model.Plant;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.service.PlantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Service
public class PlantServiceImpl implements PlantService {

  @Autowired
  private PlantDao plantDao;

  @Autowired
  private PlantConverter plantConverter;

  @Autowired
  private UserDao userDao;


  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public PlantResponse createPlant(Long userId, PlantRequest plantRequest) {
    checkExistence(userId);
    User owner = userDao.getOne(userId);
    Plant plant = plantConverter.toModel(plantRequest);
    plant.setCreationDate(LocalDateTime.now());
    plant.setOwner(owner);
    Plant saved = plantDao.save(plant);
    PlantResponse plantResponse = plantConverter.toDto(saved);
    plantResponse.setName(null);
    plantResponse.setImage(null);
    plantResponse.setLocation(null);
    plantResponse.setSpecies(null);
    plantResponse.setMoistureThreshold(null);
    return plantResponse;
  }

  @Override
  @Transactional(readOnly = true)
  public ItemList<PlantResponse> findPlants(PlantFilter plantFilter, Long userId) {
    Page<Plant> plants = plantDao.findPlantByFilter(plantFilter.getName(), userId, plantFilter);
    List<PlantResponse> plantResponses = plantConverter.toDtoList(plants.getContent());
    return new ItemList<>(plantResponses, plants.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public PlantResponse getPlant(Long userId, Long plantId) {
    checkExistence(userId);
    checkPlantExistence(plantId);
    Plant plant = plantDao.getPlantByUser(plantId, userId)
            .orElseThrow(() -> new PlantCareException("User with id [" + userId + "] does not have plant with id [" + plantId + "]", HttpStatus.NOT_FOUND));
    return plantConverter.toDto(plant);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void editPlant(Long userId, Long plantId, PlantRequest plantRequest) {
    checkExistence(userId);
    checkPlantExistence(plantId);
    Plant plant = plantDao.getPlantByUser(plantId, userId)
            .orElseThrow(() -> new PlantCareException("User with id [" + userId + "] does not have plant with id [" + plantId + "]", HttpStatus.NOT_FOUND));

    plant.merge(plantRequest);
    plantDao.save(plant);
  }


  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deletePlant(Long userId, Long plantId) {
    checkExistence(userId);
    checkPlantExistence(plantId);
    User owner = userDao.getOne(userId);
    Plant plant = plantDao.getPlantByUser(plantId, userId)
            .orElseThrow(() -> new PlantCareException("User with id [" + userId + "] does not have plant with id [" + plantId + "]", HttpStatus.NOT_FOUND));

    //TODO make appropriate methods in User class
    plant.setOwner(null);
    owner.getPlants().remove(plant);
    plantDao.delete(plant);
  }

  private void checkExistence(Long userId) {
    if (!userDao.exists(userId)) {
      throw new PlantCareException("User with id [" + userId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }

  private void checkPlantExistence(Long plantId) {
    if (!plantDao.exists(plantId)) {
      throw new PlantCareException("Plant with id [" + plantId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }
}
