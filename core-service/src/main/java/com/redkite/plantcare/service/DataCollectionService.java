package com.redkite.plantcare.service;


import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.model.nosql.LastPeriod;

public interface DataCollectionService {

  void logData(LogDataRequest data);

  LogDataResponse getDataFromPeriodOfTime(SensorDataFilter filter);

  void deleteDataBySensorId(Long sensorId);

  LogDataResponse getDataForLastPeriodOfTime(LastPeriod lastPeriod);

}
