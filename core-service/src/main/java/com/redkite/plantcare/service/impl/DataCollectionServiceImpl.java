package com.redkite.plantcare.service.impl;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.LogDataRequest;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.convertors.LogDataConverter;
import com.redkite.plantcare.dao.PlantDao;
import com.redkite.plantcare.dao.SensorDao;
import com.redkite.plantcare.dao.impl.LogDataDao;
import com.redkite.plantcare.model.Plant;
import com.redkite.plantcare.model.Sensor;
import com.redkite.plantcare.model.nosql.PlantLogData;
import com.redkite.plantcare.service.DataCollectionService;
import com.redkite.plantcare.service.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {

  @Autowired
  private SensorService sensorService;

  //TODO change to service
  @Autowired
  private SensorDao sensorDao;

  //TODO change to service
  @Autowired
  private PlantDao plantDao;

  @Autowired
  private LogDataDao logDataDao;

  @Autowired
  private LogDataConverter logDataConverter;


  @Override
  public void logData(LogDataRequest logData) {
    if (sensorService.isActive(logData.getSensorId())) {
      if (logData.getLogTime() == null) {
        logData.setLogTime(LocalDateTime.now());
      }
      PlantLogData plantLogData = logDataConverter.toModel(logData);
      Sensor sensor = sensorDao.getOne(logData.getSensorId());
      for (Plant plant : sensor.getPlants()) {
        plantLogData.setPlantId(plant.getId());
        logDataDao.save(plantLogData);
      }
      sensorService.checkExceedThreshold(logData.getSensorId(), logData.getValue(), logData.getDataType());
    } else {
      throw new PlantCareException("Sensor with id [" + logData.getSensorId() + "] is not activated",
              HttpStatus.PRECONDITION_REQUIRED);
    }
  }

  @Override
  public LogDataResponse getDataFromPeriodOfTime(SensorDataFilter filter) {
    return logDataDao.findByFilter(filter);
  }

  @Override
  public void deleteDataByPlantId(Long plantId) {
    logDataDao.deleteByPlantId(plantId);
  }

}
