package com.redkite.plantcare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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

  @Column(name = "data_type")
  private String dataType;


  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  public void setOwner(User user) {
    setOwner(user, true);
  }

  void setOwner(User user, boolean bi) {
    if (user != null) {
      this.owner = user;
      if (bi) {
        owner.addSensor(this, false);
      }
    }

  }

  @ManyToMany
  @JoinTable(name = "plants_sensors_binding",
          joinColumns = {@JoinColumn(name = "sensor_id")},
          inverseJoinColumns = {@JoinColumn(name = "plant_id")})
  private Set<Plant> plants;

}
