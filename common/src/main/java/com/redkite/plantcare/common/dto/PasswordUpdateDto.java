package com.redkite.plantcare.common.dto;

import com.redkite.plantcare.common.constraint.Password;

import lombok.Data;

@Data
public class PasswordUpdateDto {

  @Password
  private String oldPassword;

  @Password
  private String newPassword;

}
