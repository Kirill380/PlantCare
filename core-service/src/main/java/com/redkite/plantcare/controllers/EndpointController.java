package com.redkite.plantcare.controllers;

import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.convertors.LogDataConverter;
import com.redkite.plantcare.dao.LogDataDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EndpointController {

  @Autowired
  private LogDataDao logDataDao;

  @Autowired
  private LogDataConverter logDataConverter;

  @RequestMapping(value = "/endpoints/data", method = RequestMethod.POST)
  public void logData(@RequestBody LogDataRequest logData) {
    logData.setLogTime(LocalDateTime.now());
    logDataDao.save(logDataConverter.toModel(logData));
  }

  @RequestMapping(value = "/endpoints/data", method = RequestMethod.GET)
  public LogDataResponse getFromPeriodOfTime(
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    return logDataDao.findByFilter(new SensorDataFilter(from, to));
  }

}
