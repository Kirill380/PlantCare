package com.redkite.plantcare.service.impl;


import static com.google.common.collect.ImmutableMap.of;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.FirmwareSettings;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.NotificationRequest;
import com.redkite.plantcare.common.dto.NotificationType;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.controllers.filters.SensorFilter;
import com.redkite.plantcare.convertors.PlantConverter;
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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("sensorService")
public class SensorServiceImpl implements SensorService {

  private static final String MOISTURE_SENSOR_FIRMWARE_TEMPLATE = "moisture_sensor_firmware.ftl";

  @Autowired
  private SensorDao sensorDao;

  @Autowired
  private NotificationSender sender;

  @Autowired
  private SensorConverter sensorConverter;

  @Autowired
  private PlantConverter plantConverter;

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
    return generateFirmware(sensorRequest, saved.getId());
  }


  private String generateFirmware(SensorRequest sensorRequest, Long sensorId) throws IOException, TemplateException {
    FirmwareSettings firmwareSettings = new FirmwareSettings();
    firmwareSettings.setWifiName(sensorRequest.getWifiName());
    firmwareSettings.setWifiPassword(sensorRequest.getWifiPassword());
    firmwareSettings.setServerAddress(InetAddress.getLocalHost().getHostAddress());
    firmwareSettings.setLogFrequency(sensorRequest.getLogFrequency());
    firmwareSettings.setServerPort(serverPort);
    firmwareSettings.setSensorId(sensorId);
    firmwareSettings.setDataType(sensorRequest.getDataType());
    Template template = freemarkerConfig.getTemplate(MOISTURE_SENSOR_FIRMWARE_TEMPLATE);
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, firmwareSettings);
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
  public SensorResponse getSensor(Long sensorId) {
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));
    return sensorConverter.toDto(sensor);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public String editSensor(Long sensorId, SensorRequest sensorRequest) throws IOException, TemplateException {
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));
    sensor.merge(sensorRequest);
    sensorDao.save(sensor);
    return generateFirmware(sensorRequest, sensorId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isActive(Long sensorId) {
    checkSensorExistence(sensorId);
    return sensorDao.isActivated(sensorId);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void checkExceedThreshold(Long sensorId, Integer value, String dataType) {
    Sensor sensor = sensorDao.getOne(sensorId);
    User owner = sensor.getOwner();
    //TODO find out this information using SQL query not Java
    List<Plant> plants = sensor.getPlants().stream()
            .filter(p -> p.getMoistureThreshold() > value)
            .collect(Collectors.toList());
    for (Plant plant : plants) {
      if (plant.getLastExceededThreshold() == null
              || ChronoUnit.MINUTES.between(LocalDateTime.now(), plant.getLastExceededThreshold()) > notificationsRepeat) {
        //TODO refactor change this field via SQL query
        plant.setLastExceededThreshold(LocalDateTime.now());
        plantDao.save(plant);
        sender.sendNotification(
                new NotificationRequest(
                        owner.getEmail(),
                        of("plantName", plant.getName(), "location", plant.getLocation()),
                        NotificationType.THRESHOLD_EXCEEDED
                ));
      }
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void bindToPlant(Long sensorId, Long plantId) {
    checkPlantExistence(plantId);
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Plant plant = plantDao.getPlantByUser(plantId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have plant with id [" + plantId + "]",
                    HttpStatus.NOT_FOUND));

    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));

    //TODO create appropriate add method
    if (!sensor.getPlants().add(plant)) {
      log.warn("Sensor [{}] already has plant with id [{}]", sensorId, plantId);
    } else {
      //TODO replace with appropriate add method
      plant.getSensors().add(sensor);
      sensor.setStatus(SensorStatus.ACTIVATED);
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void unbindFromPlant(Long sensorId, Long plantId) {
    checkPlantExistence(plantId);
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Plant plant = plantDao.getPlantByUser(plantId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have plant with id [" + plantId + "]",
                    HttpStatus.NOT_FOUND));

    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));

    //TODO create appropriate remove method
    if (!sensor.getPlants().remove(plant)) {
      log.warn("Sensor [{}] does not have plant with id [{}]", sensorId, plantId);
    }

    //TODO replace with appropriate remove method
    plant.getSensors().remove(sensor);

    if (sensor.getPlants().isEmpty()) {
      sensor.setStatus(SensorStatus.INACTIVE);
    }
  }

  @Override
  public List<PlantResponse> getPlantsBySensor(Long sensorId) {
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));

    return plantConverter.toDtoList(new ArrayList<>(sensor.getPlants()));
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deleteSensor(Long sensorId) {
    checkSensorExistence(sensorId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Sensor sensor = sensorDao.getSensorByUser(sensorId, currentUser.getUserId())
            .orElseThrow(() -> new PlantCareException("User with id [" + currentUser.getUserId() + "] does not have sensor with id [" + sensorId + "]",
                    HttpStatus.NOT_FOUND));

    Set<Plant> plants = sensor.getPlants();

    //TODO figure out best practice how to delete in case of bidirectional relations
    for (Plant plant : plants) {
      plant.getSensors().remove(sensor);
      plantDao.save(plant);
    }

    User owner = sensor.getOwner();
    owner.getSensors().remove(sensor);
    sensor.setOwner(null);
    sensorDao.delete(sensor);
  }

  private boolean isSensorBelongsToUser(Long sensorId, Long userId) {
    return sensorDao.getSensorByUser(sensorId, userId).isPresent();
  }


  private void checkPlantExistence(Long plantId) {
    if (!plantDao.exists(plantId)) {
      throw new PlantCareException("Plant with id [" + plantId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }

  private void checkSensorExistence(Long sensorId) {
    if (!sensorDao.exists(sensorId)) {
      throw new PlantCareException("Sensor with id [" + sensorId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }
}
