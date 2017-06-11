package com.redkite.plantcare.controllers;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ErrorDto;
import com.redkite.plantcare.common.dto.FieldErrorDto;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.PlantRequest;
import com.redkite.plantcare.common.dto.PlantResponse;
import com.redkite.plantcare.common.dto.SensorResponse;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.PlantFilter;
import com.redkite.plantcare.controllers.filters.UserFilter;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.service.PlantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

  @Autowired
  private PlantService plantService;

  @RequestMapping(method = RequestMethod.POST)
  public PlantResponse createPlant(@Validated @RequestBody PlantRequest plantRequest,
                                 BindingResult result) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during plant creation", result), HttpStatus.BAD_REQUEST);
    }
    return plantService.createPlant(currentUser.getUserId(), plantRequest);
  }

  
  @RequestMapping(method = RequestMethod.GET)
  public ItemList<PlantResponse> getPlants(@ModelAttribute PlantFilter filter) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return plantService.findPlants(filter, currentUser.getUserId());
  }

  private ErrorDto getErrors(String errorMessage, BindingResult result) {
    List<FieldErrorDto> fieldErrors = result.getFieldErrors().stream()
            .map(er -> new FieldErrorDto(er.getField(), er.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorDto(errorMessage, fieldErrors);
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public PlantResponse getPlant(@PathVariable("id") Long plantId) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return plantService.getPlant(currentUser.getUserId(), plantId);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void editPlant(@PathVariable("id") Long plantId,
                       @RequestBody @Validated PlantRequest plantRequest,
                       BindingResult result) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during plant creation", result), HttpStatus.BAD_REQUEST);
    }
    plantService.editPlant(currentUser.getUserId(), plantId, plantRequest);
  }


  @RequestMapping(value = "/{plantId}/sensors", method = RequestMethod.GET)
  public List<SensorResponse> getSensorsByPlant(@PathVariable("plantId") Long plantId) {
    return plantService.getSensorsByPlant(plantId);
  }
 

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deletePlant(@PathVariable("id") Long plantId) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    plantService.deletePlant(currentUser.getUserId(), plantId);
  }
}
