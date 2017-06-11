package com.redkite.plantcare.model.nosql;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.Data;

import java.util.Date;


@Data
@Table(name = "plant_data")
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class PlantLogData {

  @PartitionKey
  @Column(name = "plant_id")
  private Long plantId;

  @Column(name = "data_type")
  private String dataType;

  @Column
  private Integer value;

  @ClusteringColumn
  @Column(name = "log_time")
  private Date logTime;


  public Date getLogTime() {
    return new Date(logTime.getTime());
  }

  public void setLogTime(Date logTime) {
    this.logTime = new Date(logTime.getTime());
  }
}
