package com.redkite.plantcare.service.impl;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.model.nosql.LastPeriod;
import com.redkite.plantcare.service.DataCollectionService;

import org.springframework.stereotype.Service;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {

  @Override
  public void logData(LogDataRequest data) {

  }

  @Override
  public LogDataResponse getDataFromPeriodOfTime(SensorDataFilter filter) {
    return null;
  }

  @Override
  public void deleteDataBySensorId(Long sensorId) {

  }

  @Override
  public LogDataResponse getDataForLastPeriodOfTime(LastPeriod lastPeriod) {
    return null;
  }
}
