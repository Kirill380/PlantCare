package com.redkite.plantcare.controllers;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.service.DataCollectionService;

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
  private DataCollectionService dataCollectionService;


  @RequestMapping(value = "/api/sensors/data", method = RequestMethod.POST)
  public void logData(@RequestBody LogDataRequest logData) {
    dataCollectionService.logData(logData);
  }

  @RequestMapping(value = "/api/sensors/data", method = RequestMethod.GET)
  public LogDataResponse getFromPeriodOfTime(
          @RequestParam Long plantId,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    return dataCollectionService.getDataFromPeriodOfTime(new SensorDataFilter(plantId, from, to));
  }

}
