package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.model.Sensor;

import org.springframework.stereotype.Component;

import java.util.Base64;


@Component
public class SensorConverter implements ToModelConverter<Sensor, SensorRequest>, ToDtoConverter<Sensor, SensorResponse> {

  @Override
  public Sensor toModel(SensorRequest dto) {
    Sensor sensor = new Sensor();
    sensor.setName(dto.getName());
    sensor.setDataType(dto.getDataType());
    sensor.setLogFrequency(dto.getLogFrequency());
    return sensor;
  }

  @Override
  public SensorResponse toDto(Sensor model) {
    SensorResponse sensorResponse = new SensorResponse();
    sensorResponse.setId(model.getId());
    sensorResponse.setName(model.getName());
    sensorResponse.setLogFrequency(model.getLogFrequency());
    sensorResponse.setDataType(model.getDataType());
    sensorResponse.setStatus(model.getStatus().name());
    sensorResponse.setCreationDate(model.getCreationDate());
    return sensorResponse;
  }

}
