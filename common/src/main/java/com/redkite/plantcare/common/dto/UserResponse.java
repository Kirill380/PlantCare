package com.redkite.plantcare.common.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

  private Long id;

  private String email;

  private String firstName;

  private String lastName;

}
