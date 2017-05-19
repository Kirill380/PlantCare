package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.Sensor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDao extends JpaRepository<Sensor, Long> {
}
