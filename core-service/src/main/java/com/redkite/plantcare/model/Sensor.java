package com.redkite.plantcare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ToString(of = {"id", "name", "status"})
@EqualsAndHashCode(of = {"id"})
public class Sensor {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  @Column
  @Enumerated(EnumType.STRING)
  private SensorStatus status;

  @Column(name = "log_frequency")
  private Integer logFrequency;

  @Column(name = "moisture_threshold")
  private Integer moistureThreshold;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;


}
