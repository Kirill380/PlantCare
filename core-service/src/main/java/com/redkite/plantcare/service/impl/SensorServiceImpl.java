package com.redkite.plantcare.service.impl;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;
import com.redkite.plantcare.dao.SensorDao;
import com.redkite.plantcare.notifications.senders.NotificationSender;
import com.redkite.plantcare.service.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorServiceImpl implements SensorService {

  @Autowired
  private SensorDao sensorDao;

  @Autowired
  private NotificationSender sender;

  @Override
  public String createSensor(SensorRequest sensorRequest) {
    return null;
  }

  @Override
  public ItemList<SensorResponse> findSensors(SensorFilter sensorFilter) {
    return null;
  }

  @Override
  public SensorResponse getSensor(Long id) {
    return null;
  }

  @Override
  public String editSensor(Long id, SensorRequest sensorRequest) {
    return null;
  }

  @Override
  public boolean isActive(Long id) {
    return false;
  }

  @Override
  public void checkExceedTrashHold(Long sensorId, Integer value, String dataType) {

  }

  @Override
  public void bindToPlant(Long sensorId, Long plantId) {

  }

  @Override
  public void unbindFromPlant(Long sensorId, Long plantId) {

  }


  @Override
  public void deleteSensor(Long id) {

  }
}
