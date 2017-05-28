package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.model.nosql.SensorLogData;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class LogDataConverter implements ToModelConverter<SensorLogData, LogDataRequest>, ToDtoConverter<SensorLogData, LogDataRequest> {

  @Override
  public SensorLogData toModel(LogDataRequest dto) {
    SensorLogData sensorLogData = new SensorLogData();
    sensorLogData.setSensorId(dto.getSensorId());
    sensorLogData.setValue(dto.getValue());
    sensorLogData.setLogTime(Date.from(dto.getLogTime().atZone(ZoneId.systemDefault()).toInstant()));
    sensorLogData.setDataType(dto.getDataType());
    return sensorLogData;
  }

  @Override
  public LogDataRequest toDto(SensorLogData model) {
    LogDataRequest logDataRequest = new LogDataRequest();
    logDataRequest.setValue(model.getValue());
    logDataRequest.setLogTime(LocalDateTime.ofInstant(model.getLogTime().toInstant(), ZoneId.systemDefault()));
    return logDataRequest;
  }
}
