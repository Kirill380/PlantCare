package com.redkite.plantcare.service;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.UserFilter;
import com.redkite.plantcare.model.User;

public interface UserService {

  UserResponse createUser(UserRequest userRequest);

  ItemList<UserResponse> findUsers(UserFilter filter);

  User getUserByEmail(String email);

  UserResponse getUser(Long userId);

  void editUser(Long userId, UserRequest userRequest);

  void deleteUser(Long userId);

  boolean checkPasswordMatching(String email, String password);

}
