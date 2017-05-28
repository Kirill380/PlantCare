package com.redkite.plantcare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

  @Column(name = "plant_image")
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


  public void setImage(byte[] image) {
    this.image = Arrays.copyOf(image, image.length);
  }

  public byte[] getImage() {
    return Arrays.copyOf(this.image, image.length);
  }

  @ManyToMany
  @JoinTable(joinColumns = {@JoinColumn(name = "sensor_id")},
          inverseJoinColumns = {@JoinColumn(name = "plant_id")})
  private List<Sensor> sensors;
}
