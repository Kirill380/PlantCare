package com.redkite.plantcare.common.dto;


import com.redkite.plantcare.common.constraint.Email;
import com.redkite.plantcare.common.constraint.Password;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {

  @Email
  private String email;

  @Password
  private String password;
}
