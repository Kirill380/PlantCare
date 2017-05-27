package com.redkite.plantcare.configurations;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSettings {

  private Integer expTime;

  private Integer refreshExpTime;

  private String tokenIssuer;

  private String tokenSigningKey;
}
