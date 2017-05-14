package com.redkite.plantcare.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column(name = "plants_limit")
    private Integer limitOfPlants;

    @Column(name = "data_limit")
    private Integer limitOfPrecessingData;
}
