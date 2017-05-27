package com.redkite.plantcare.common.dto;


import lombok.Data;

@Data
public class TokensDto {

  private String accessToken;

  private String refreshToken;

}
