package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.Plant;
import com.redkite.plantcare.model.Sensor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlantDao extends JpaRepository<Plant, Long> {

  @Query(value = "SELECT p FROM Plant p JOIN p.owner o where (p.name like %:name% OR :name = NULL) and o.id = :userId",
          countQuery = "SELECT count(p) FROM Plant p JOIN p.owner o where (p.name like %:name% OR :name = NULL) and o.id = :userId")
  Page<Plant> findPlantByFilter(@Param("name") String name, @Param("userId") Long userId, Pageable pageable);


  @Query("SELECT p FROM Plant p JOIN p.owner u where p.id = ?1 and u.id = ?2")
  Optional<Plant> getPlantByUser(Long plantId, Long userId);
}
