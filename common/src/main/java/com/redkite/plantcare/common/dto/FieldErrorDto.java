package com.redkite.plantcare.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorDto {

  private String fieldName;

  private String errorMessage;
}
