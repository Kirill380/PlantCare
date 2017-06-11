package com.redkite.plantcare.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.asc;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.convertors.LogDataConverter;
import com.redkite.plantcare.model.nosql.PlantLogData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class LogDataDao extends AbstractCassandraDao<PlantLogData, Long> {

  @Autowired
  private LogDataConverter logDataConverter;

  @Override
  protected Class<PlantLogData> getColumnFamilyClass() {
    return PlantLogData.class;
  }

  @Override
  protected String getColumnFamilyName() {
    return "plant_data";
  }

  @Override
  public PlantLogData save(PlantLogData entity) {
    return super.save(entity);
  }

  public void deleteByPlantId(Long plantId) {
    execute(QueryBuilder.delete().all()
            .from(getColumnFamilyName())
            .where(eq("plant_id", plantId))
            .setConsistencyLevel(getConsistencyLevel())
    );
  }

  public LogDataResponse findByFilter(SensorDataFilter filter) {
    List<PlantLogData> data;
    if (filter.getFrom() == null && filter.getTo() == null) {
      data = execute(QueryBuilder.select()
              .all()
              .from(getColumnFamilyName())
              .where(eq("plant_id", filter.getPlantId()))
              .orderBy(asc("log_time"))
              .setConsistencyLevel(getConsistencyLevel())
      );
    } else {
      Date from = Date.from(filter.getFrom().atZone(ZoneId.systemDefault()).toInstant());
      Date to = Date.from(filter.getTo().atZone(ZoneId.systemDefault()).toInstant());
      data = execute(QueryBuilder.select()
              .all()
              .from(getColumnFamilyName())
              .where(eq("plant_id", filter.getPlantId()))
              .and(gte("log_time", from))
              .and(lte("log_time", to))
              .orderBy(asc("log_time"))
              .setConsistencyLevel(getConsistencyLevel())
      );
    }
    LogDataResponse logDataResponse = new LogDataResponse();
    logDataResponse.putAll(logDataConverter.toDtoList(data));
    return logDataResponse;
  }

}
