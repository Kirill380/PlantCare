package com.redkite.plantcare.controllers;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ErrorDto;
import com.redkite.plantcare.common.dto.FieldErrorDto;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorRequest;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.controllers.filters.PlantFilter;
import com.redkite.plantcare.controllers.filters.SensorFilter;
import com.redkite.plantcare.model.Sensor;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.service.SensorService;

import freemarker.template.TemplateException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

  @Autowired
  private SensorService sensorService;


  @RequestMapping(method = RequestMethod.POST, produces = "text/plain")
  public ResponseEntity<String> createSensor(@Validated @RequestBody SensorRequest sensorRequest,
                                     BindingResult result) throws IOException, TemplateException {
    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during sensor creation", result), HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(sensorService.createSensor(sensorRequest));
  }


  @RequestMapping(method = RequestMethod.GET)
  public ItemList<SensorResponse> getSensors(@ModelAttribute SensorFilter filter) {
    return sensorService.findSensors(filter);
  }


  @RequestMapping(value = "/{sensorId}", method = RequestMethod.GET)
  public SensorResponse getSensor(@PathVariable("sensorId") Long sensorId) {
    return sensorService.getSensor(sensorId);
  }

  @RequestMapping(value = "/{sensorId}", method = RequestMethod.PUT)
  public void editSensor(@PathVariable("sensorId") Long sensorId,
                        @RequestBody @Validated SensorRequest sensorRequest,
                        BindingResult result) throws IOException, TemplateException {
    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during updating sensor profile", result), HttpStatus.BAD_REQUEST);
    }
    sensorService.editSensor(sensorId, sensorRequest);
  }

  @RequestMapping(value = "/{sensorId}/plants", method = RequestMethod.GET)
  public List<PlantResponse> getPlantsBySensor(@PathVariable("sensorId") Long sensorId) {
    return sensorService.getPlantsBySensor(sensorId);
  }

  @RequestMapping(value = "/{sensorId}/plants/{plantId}", method = RequestMethod.PUT)
  public void bindSensorToPlant(@PathVariable("sensorId") Long sensorId,
                                @PathVariable("plantId") Long plantId) {
    sensorService.bindToPlant(sensorId, plantId);

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{sensorId}/plants/{plantId}", method = RequestMethod.DELETE)
  public void unbindSensorFromPlant(@PathVariable("sensorId") Long sensorId,
                                    @PathVariable("plantId") Long plantId) {
    sensorService.unbindFromPlant(sensorId, plantId);
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deleteSensor(@PathVariable("id") Long sensorId) {
    sensorService.deleteSensor(sensorId);
  }


  private ErrorDto getErrors(String errorMessage, BindingResult result) {
    List<FieldErrorDto> fieldErrors = result.getFieldErrors().stream()
            .map(er -> new FieldErrorDto(er.getField(), er.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorDto(errorMessage, fieldErrors);
  }


}
