package com.redkite.plantcare.service;

import com.redkite.plantcare.common.dto.UserList;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.model.User;

public interface UserService {

  UserResponse createUser(UserRequest userRequest);

  UserList getUsers();

  User getUserByEmail(String email);

  UserResponse getUser(Long userId);

  void editUser(UserRequest userRequest);

  void deleteUser(UserRequest userRequest);

  boolean checkPasswordMatching(String email, String password);

}
