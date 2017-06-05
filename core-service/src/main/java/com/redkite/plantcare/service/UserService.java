package com.redkite.plantcare.service;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.UserFilter;
import com.redkite.plantcare.model.User;

import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

  UserResponse createUser(UserRequest userRequest);

  @PreAuthorize("hasAuthority('admin')")
  ItemList<UserResponse> findUsers(UserFilter filter);

  //TODO refactor replace with appropriate method that doesn't return model object
  User getUserByEmail(String email);

  @PreAuthorize("hasAuthority('admin') or principal.userId == #userId")
  UserResponse getUser(Long userId);

  //TODO refactor replace with appropriate method that doesn't return model object
  User getFullUser(Long userId);

  @PreAuthorize("hasAuthority('admin') or principal.userId == #userId")
  void editUser(Long userId, UserRequest userRequest);

  @PreAuthorize("hasAuthority('admin') and principal.userId != #userId")
  void deleteUser(Long userId);

  @PreAuthorize("principal.userId == #userId")
  void changePassword(Long userId, PasswordUpdateDto passwordUpdateDto);

  boolean checkPasswordMatching(String email, String password);

}
