package com.redkite.plantcare.service;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;

public interface SensorService {

    String createSensor(SensorRequest sensorRequest);

    ItemList<SensorResponse> findSensors(SensorFilter sensorFilter);

    SensorResponse getSensor(Long id);

    String editSensor(Long id, SensorRequest sensorRequest);

    boolean isActive(Long id);

    void deleteSensor(Long id);

}
