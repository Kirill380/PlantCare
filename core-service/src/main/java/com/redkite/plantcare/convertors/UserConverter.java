package com.redkite.plantcare.convertors;

import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

@Component
public class UserConverter implements ToModelConverter<User, UserRequest>, ToDtoConverter<User, UserResponse> {

  // @Autowired
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public User toModel(UserRequest dto) {
    User user = new User();
    user.setEmail(dto.getEmail());
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
    return user;
  }

  @Override
  public UserResponse toDto(User model) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(model.getId());
    userResponse.setEmail(model.getEmail());
    userResponse.setFirstName(model.getFirstName());
    userResponse.setLastName(model.getLastName());
    userResponse.setCreationDate(model.getCreationDate());
    userResponse.setRoles(Arrays.asList(model.getRole().getName()));
    return userResponse;
  }
}
