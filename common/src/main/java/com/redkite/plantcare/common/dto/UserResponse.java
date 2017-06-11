package com.redkite.plantcare.common.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserResponse {

  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private LocalDateTime creationDate;

  private List<String> roles;

}
