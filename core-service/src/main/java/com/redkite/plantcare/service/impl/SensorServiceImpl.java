package com.redkite.plantcare.service.impl;


import static com.google.common.collect.ImmutableMap.of;

import com.redkite.plantcare.common.dto.FirmwareSettings;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.NotificationRequest;
import com.redkite.plantcare.common.dto.NotificationType;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;
import com.redkite.plantcare.convertors.SensorConverter;
import com.redkite.plantcare.convertors.UserConverter;
import com.redkite.plantcare.dao.PlantDao;
import com.redkite.plantcare.dao.SensorDao;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.model.Plant;
import com.redkite.plantcare.model.Sensor;
import com.redkite.plantcare.model.SensorStatus;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.notifications.senders.NotificationSender;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.service.DataCollectionService;
import com.redkite.plantcare.service.SensorService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service("sensorService")
public class SensorServiceImpl implements SensorService {

  @Autowired
  private SensorDao sensorDao;

  @Autowired
  private NotificationSender sender;

  @Autowired
  private DataCollectionService dataCollectionService;

  @Autowired
  private SensorConverter sensorConverter;

  @Autowired
  private UserDao userDao;

  @Autowired
  private PlantDao plantDao;


  @Value("${server.port}")
  private Integer serverPort;

  @Value("${notifications.repeat}")
  private Integer notificationsRepeat;


  @Autowired
  private Configuration freemarkerConfig;

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public String createSensor(SensorRequest sensorRequest) throws IOException, TemplateException {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userDao.getOne(currentUser.getUserId());
    Sensor sensor = sensorConverter.toModel(sensorRequest);
    sensor.setCreationDate(LocalDateTime.now());
    sensor.setStatus(SensorStatus.INACTIVE);
    sensor.setOwner(user);
    Sensor saved = sensorDao.save(sensor);

    FirmwareSettings firmwareSettings = new FirmwareSettings();
    firmwareSettings.setWifiName(sensorRequest.getWifiName());
    firmwareSettings.setWifiPassword(sensorRequest.getWifiPassword());
    firmwareSettings.setServerAddress( InetAddress.getLocalHost().getHostAddress());
    firmwareSettings.setLogFrequency(sensorRequest.getLogFrequency());
    firmwareSettings.setServerPort(serverPort);
    firmwareSettings.setSensorId(saved.getId());
    firmwareSettings.setDataType(sensorRequest.getDataType());

    Template template = freemarkerConfig.getTemplate("moisture_sensor_firmware.ftl");
    String firmware = FreeMarkerTemplateUtils.processTemplateIntoString(template, firmwareSettings);
    return firmware;
  }

  @Override
  @Transactional(readOnly = true)
  public ItemList<SensorResponse> findSensors(SensorFilter sensorFilter) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Page<Sensor> sensors = sensorDao.findSensorsByFilter(sensorFilter.getName(), currentUser.getUserId(), sensorFilter);
    List<SensorResponse> plantResponses = sensorConverter.toDtoList(sensors.getContent());
    return new ItemList<>(plantResponses, sensors.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public SensorResponse getSensor(Long id) {
    return null;
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public String editSensor(Long id, SensorRequest sensorRequest) {
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isActive(Long id) {
    //TODO find out this information using SQL query not Java
    return sensorDao.getOne(id).getStatus() == SensorStatus.ACTIVATED;
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void checkExceedThreshold(Long sensorId, Integer value, String dataType) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userDao.getOne(currentUser.getUserId());
    Sensor sensor = sensorDao.getOne(sensorId);
    List<Plant> plants = sensor.getPlants().stream()
            .filter(p -> p.getMoistureThreshold() > value)
            .collect(Collectors.toList());
    for (Plant plant : plants) {
      if(plant.getLastExceededThreshold() == null ||
              ChronoUnit.MINUTES.between(LocalDateTime.now(),plant.getLastExceededThreshold()) > notificationsRepeat) {
        //TODO refactor change this field via SQL query
        plant.setLastExceededThreshold(LocalDateTime.now());
        plantDao.save(plant);
        sender.sendNotification(
                new NotificationRequest(
                        user.getEmail(),
                        of("plantName", plant.getName(), "location", plant.getLocation()),
                        NotificationType.THRESHOLD_EXCEEDED
                ));
      }
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void bindToPlant(Long sensorId, Long plantId) {

  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void unbindFromPlant(Long sensorId, Long plantId) {

  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deleteSensor(Long id) {

  }

  public boolean isSensorBelongsToUser(Long sensorId, Long userId) {
    return sensorDao.getSensorByUser(sensorId, userId).isPresent();
  }

}
