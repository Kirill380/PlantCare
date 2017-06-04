package com.redkite.plantcare.common.dto;

import com.redkite.plantcare.common.constraint.Email;
import com.redkite.plantcare.common.constraint.Password;

import lombok.Data;

import javax.validation.groups.Default;

@Data
public class UserRequest {

  @Email(groups = {Default.class})
  private String email;

  private String firstName;

  private String lastName;

  @Password(groups = {Default.class})
  private String password;


  public interface UserUpdate {}
}
