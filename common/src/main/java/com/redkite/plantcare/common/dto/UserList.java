package com.redkite.plantcare.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserList {

  private final List<UserResponse> users;

  private final Long count;

  public UserList(List<UserResponse> users, Long count) {
    this.users = new ArrayList<>(users);
    this.count = count;
  }
}
