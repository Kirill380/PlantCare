package com.redkite.plantcare.controllers;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ErrorDto;
import com.redkite.plantcare.common.dto.FieldErrorDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;


  /**
   * Creates new user based on user request.
   *
   * @param userRequest the dto that contains user info
   * @return the user response dto
   */
  @RequestMapping(method = RequestMethod.POST)
  public UserResponse createUser(@Validated @RequestBody UserRequest userRequest,
                                 BindingResult result) {
    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }
    return userService.createUser(userRequest);
  }

  private ErrorDto getErrors(String errorMessage, BindingResult result) {
    List<FieldErrorDto> fieldErrors = result.getFieldErrors().stream()
            .map(er -> new FieldErrorDto(er.getField(), er.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorDto(errorMessage, fieldErrors);
  }
}
