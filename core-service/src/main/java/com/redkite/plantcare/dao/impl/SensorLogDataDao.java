package com.redkite.plantcare.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.asc;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.redkite.plantcare.common.dto.LogDataResponse;
import com.redkite.plantcare.common.dto.SensorDataFilter;
import com.redkite.plantcare.convertors.LogDataConverter;
import com.redkite.plantcare.model.nosql.SensorLogData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class SensorLogDataDao extends AbstractCassandraDao<SensorLogData, Long> {

  @Autowired
  private LogDataConverter logDataConverter;

  @Override
  protected Class<SensorLogData> getColumnFamilyClass() {
    return SensorLogData.class;
  }

  @Override
  protected String getColumnFamilyName() {
    return "sensor_data";
  }

  @Override
  public SensorLogData save(SensorLogData entity) {
    return super.save(entity);
  }

  public LogDataResponse findByFilter(SensorDataFilter filter) {
    List<SensorLogData> data;
    if (filter == null) {
      data = findAll();
      //TODO sort on cassandra side not in java
      data.sort((ld1, ld2) -> ld1.getLogTime().compareTo(ld2.getLogTime()));
    } else {
      Date from = Date.from(filter.getFrom().atZone(ZoneId.systemDefault()).toInstant());
      Date to = Date.from(filter.getTo().atZone(ZoneId.systemDefault()).toInstant());
      data = execute(QueryBuilder.select()
              .all()
              .from(getColumnFamilyName())
              .where(eq("sensor_id", filter.getSensorId()))
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
