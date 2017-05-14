package com.redkite.plantcare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @Column
    @ManyToMany
    @JoinTable(joinColumns = {@JoinColumn(name = "sensor_id")},
            inverseJoinColumns = {@JoinColumn(name = "plant_id")})
    private List<Sensor> sensors;
}
