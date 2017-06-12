package com.redkite.plantcare.common.dto;


import lombok.Data;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
public class LogDataResponse {

  private List<TimeValuePair> dataTimeSeries = new ArrayList<>();

  public void put(LogDataRequest logDataRequest) {
    long milli = logDataRequest.getLogTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    dataTimeSeries.add(new TimeValuePair(milli, logDataRequest.getValue()));
  }

  public void putAll(List<LogDataRequest> list) {
    list.forEach(this::put);
  }

}
