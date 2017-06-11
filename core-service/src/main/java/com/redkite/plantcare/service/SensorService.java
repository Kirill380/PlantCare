package com.redkite.plantcare.service;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;

import freemarker.template.TemplateException;

import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.List;

public interface SensorService {

  @PreAuthorize("hasAuthority('regularUser')")
  String createSensor(SensorRequest sensorRequest) throws IOException, TemplateException;

  @PreAuthorize("hasAuthority('regularUser')")
  ItemList<SensorResponse> findSensors(SensorFilter sensorFilter);

  @PreAuthorize("hasAuthority('regularUser')")
  SensorResponse getSensor(Long id);

  @PreAuthorize("hasAuthority('regularUser')")
  String editSensor(Long id, SensorRequest sensorRequest);

  boolean isActive(Long id);

  void checkExceedThreshold(Long sensorId, Integer value, String dataType);

  @PreAuthorize("hasAuthority('regularUser')")
  void bindToPlant(Long sensorId, Long plantId);

  @PreAuthorize("hasAuthority('regularUser')")
  void unbindFromPlant(Long sensorId, Long plantId);

  @PreAuthorize("hasAuthority('regularUser')")
  List<PlantResponse> getPlantsBySensor(Long sensorId);

  @PreAuthorize("hasAuthority('regularUser')")
  void deleteSensor(Long id);

}
