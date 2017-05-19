package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.Plant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantDao extends JpaRepository<Plant, Long> {
}
