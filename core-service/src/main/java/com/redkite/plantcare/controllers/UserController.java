package com.redkite.plantcare.controllers;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ErrorDto;
import com.redkite.plantcare.common.dto.FieldErrorDto;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.UserFilter;
import com.redkite.plantcare.security.UserContext;
import com.redkite.plantcare.service.TokenInvalidationService;
import com.redkite.plantcare.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
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
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private TokenInvalidationService tokenInvalidationService;


  @Autowired
  private Validator validator;

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

  @RequestMapping(method = RequestMethod.GET)
  public ItemList<UserResponse> getUsers(@ModelAttribute UserFilter filter) {
    return userService.findUsers(filter);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public UserResponse getUser(@PathVariable("id") Long userId) {
    return userService.getUser(userId);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void editUser(@PathVariable("id") Long userId,
                       @RequestBody @Validated({UserRequest.UserUpdate.class}) UserRequest userRequest,
                       BindingResult result) {

    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }

    if (!checkUpdateRequest(userRequest)) {
      throw new PlantCareException("Validation failed during updating user - empty update request", HttpStatus.BAD_REQUEST);
    }
    userService.editUser(userId, userRequest);
  }

  @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
  public void changePassword(@PathVariable("id") Long userId,
                             @RequestBody @Validated PasswordUpdateDto updateDto,
                             BindingResult result) {
    if (result.hasErrors()) {
      throw new PlantCareException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }
    userService.changePassword(userId, updateDto);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    tokenInvalidationService.invalidateAllTokensForUser(currentUser.getUserId());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deleteUser(@PathVariable("id") Long userId) {
    userService.deleteUser(userId);
  }

  private boolean checkUpdateRequest(UserRequest userRequest) {
    return StringUtils.isNotBlank(userRequest.getFirstName())
            || StringUtils.isNotBlank(userRequest.getLastName());

  }
}
