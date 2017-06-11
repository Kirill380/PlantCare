package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.model.nosql.PlantLogData;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class LogDataConverter implements ToModelConverter<PlantLogData, LogDataRequest>, ToDtoConverter<PlantLogData, LogDataRequest> {

  @Override
  public PlantLogData toModel(LogDataRequest dto) {
    PlantLogData plantLogData = new PlantLogData();
    plantLogData.setPlantId(dto.getSensorId());
    plantLogData.setValue(dto.getValue());
    plantLogData.setLogTime(Date.from(dto.getLogTime().atZone(ZoneId.systemDefault()).toInstant()));
    plantLogData.setDataType(dto.getDataType());
    return plantLogData;
  }

  @Override
  public LogDataRequest toDto(PlantLogData model) {
    LogDataRequest logDataRequest = new LogDataRequest();
    logDataRequest.setValue(model.getValue());
    logDataRequest.setLogTime(LocalDateTime.ofInstant(model.getLogTime().toInstant(), ZoneId.systemDefault()));
    return logDataRequest;
  }
}
