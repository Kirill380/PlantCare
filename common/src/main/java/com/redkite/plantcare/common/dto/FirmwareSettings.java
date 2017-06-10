package com.redkite.plantcare.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirmwareSettings {

  private String wifiName;

  private String wifiPassword;

  private String serverAddress;

  private Integer logFrequency;

  private Integer serverPort;

  private Long sensorId;

  private String dataType;
}
