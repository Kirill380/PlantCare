package com.redkite.plantcare.controllers;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.convertors.LogDataConverter;
import com.redkite.plantcare.dao.SensorLogDataDao;
import com.redkite.plantcare.service.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DataCollectionController {

  @Autowired
  private SensorLogDataDao sensorLogDataDao;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private LogDataConverter logDataConverter;

  @RequestMapping(value = "/api/sensors/data", method = RequestMethod.POST)
  public void logData(@RequestBody LogDataRequest logData) {
    if (sensorService.isActive(logData.getSensorId())) {
      logData.setLogTime(LocalDateTime.now());
      sensorLogDataDao.save(logDataConverter.toModel(logData));
      sensorService.checkExceedTrashHold(logData.getSensorId(), logData.getValue(), logData.getDataType());
    }
  }

  @RequestMapping(value = "/api/sensors/data", method = RequestMethod.GET)
  public LogDataResponse getFromPeriodOfTime(
          @RequestParam Long sensorId,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    return sensorLogDataDao.findByFilter(new SensorDataFilter(sensorId, from, to));
  }

}
