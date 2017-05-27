package com.redkite.plantcare.common.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data //TODO refactor -- provide only required constructor and setters
public class LogDataResponse {

  private Map<LocalDateTime, Integer> dataTimeSeries = new LinkedHashMap<>();

  public void put(LogDataRequest logDataRequest) {
    dataTimeSeries.put(logDataRequest.getLogTime(), logDataRequest.getValue());
  }

  public void putAll(List<LogDataRequest> list) {
    list.forEach(this::put);
  }

}
