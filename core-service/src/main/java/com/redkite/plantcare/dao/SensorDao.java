package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.Plant;
import com.redkite.plantcare.model.Sensor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SensorDao extends JpaRepository<Sensor, Long> {

  @Query(value = "SELECT s FROM Sensor s JOIN s.owner o where (s.name like %:name% OR :name = NULL) and o.id = :userId",
          countQuery = "SELECT count(s) FROM Sensor s JOIN s.owner o where (s.name like %:name% OR :name = NULL) and o.id = :userId")
  Page<Sensor> findSensorsByFilter(@Param("name") String name, @Param("userId") Long userId, Pageable pageable);

  @Query("SELECT s FROM Sensor s JOIN s.owner u where s.id = ?1 and u.id = ?2")
  Optional<Sensor> getSensorByUser(Long sensorId, Long userId);
}
