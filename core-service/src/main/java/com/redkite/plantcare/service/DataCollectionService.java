package com.redkite.plantcare.service;


import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;

import org.springframework.security.access.prepost.PreAuthorize;

public interface DataCollectionService {

  void logData(LogDataRequest data);

  @PreAuthorize("hasAuthority('regularUser')")
  LogDataResponse getDataFromPeriodOfTime(SensorDataFilter filter);

  void deleteDataByPlantId(Long plantId);

}
