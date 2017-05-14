package com.redkite.plantcare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

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
