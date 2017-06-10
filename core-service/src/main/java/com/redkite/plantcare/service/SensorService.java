package com.redkite.plantcare.service;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;

import freemarker.template.TemplateException;

import java.io.IOException;

public interface SensorService {

  String createSensor(SensorRequest sensorRequest) throws IOException, TemplateException;

  ItemList<SensorResponse> findSensors(SensorFilter sensorFilter);

  SensorResponse getSensor(Long id);

  String editSensor(Long id, SensorRequest sensorRequest);

  boolean isActive(Long id);

  void checkExceedThreshold(Long sensorId, Integer value, String dataType);

  void bindToPlant(Long sensorId, Long plantId);

  void unbindFromPlant(Long sensorId, Long plantId);

  void deleteSensor(Long id);

}
