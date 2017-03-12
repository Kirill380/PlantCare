package com.redkite.plantcare.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorDto {

  private String message;

  private List<FieldErrorDto> fieldErrors;
}
