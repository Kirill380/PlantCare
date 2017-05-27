package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.model.nosql.LogData;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class LogDataConverter implements ToModelConverter<LogData, LogDataRequest>, ToDtoConverter<LogData, LogDataRequest> {

  @Override
  public LogData toModel(LogDataRequest dto) {
    LogData logData = new LogData();
    logData.setSensorId(dto.getEndpointId());
    logData.setValue(dto.getValue());
    logData.setLogTime(Date.from(dto.getLogTime().atZone(ZoneId.systemDefault()).toInstant()));
    logData.setDataType(dto.getDataType());
    return logData;
  }

  @Override
  public LogDataRequest toDto(LogData model) {
    LogDataRequest logDataRequest = new LogDataRequest();
    logDataRequest.setValue(model.getValue());
    logDataRequest.setLogTime(LocalDateTime.ofInstant(model.getLogTime().toInstant(), ZoneId.systemDefault()));
    return logDataRequest;
  }
}
