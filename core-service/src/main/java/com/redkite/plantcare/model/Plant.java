package com.redkite.plantcare.model;

import com.redkite.plantcare.common.dto.PlantRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Data
@Entity
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id"})
public class Plant {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "plant_image",length = 16777215)
  private byte[] image;

  @Column(name = "display_name")
  private String name;

  @Column
  private String species;

  @Column
  private String location;

  @Column(name = "moisture_threshold")
  private Integer moistureThreshold;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(name = "last_exceeded_threshold")
  private LocalDateTime lastExceededThreshold;

  @ManyToMany(mappedBy = "plants")
  private Set<Sensor> sensors;

  public void setOwner(User user) {
    setOwner(user, true);
  }

  void setOwner(User user, boolean bi) {
    if (user != null) {
      this.owner = user;
      if (bi) {
        owner.addPlant(this, false);
      }
    }

  }

  public Plant merge(PlantRequest plantRequest) {
    if (plantRequest.getImage() != null) {
      this.image = Base64.getDecoder().decode(plantRequest.getImage());
    }
    if (plantRequest.getName() != null) {
      this.name = plantRequest.getName();
    }
    if (plantRequest.getLocation() != null) {
      this.location = plantRequest.getLocation();
    }
    if (plantRequest.getSpecies() != null) {
      this.species = plantRequest.getSpecies();
    }
    if (plantRequest.getMoistureThreshold() != null) {
      this.moistureThreshold = plantRequest.getMoistureThreshold();
    }
    return this;
  }

}
