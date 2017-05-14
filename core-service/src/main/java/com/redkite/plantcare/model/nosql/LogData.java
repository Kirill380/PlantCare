package com.redkite.plantcare.model.nosql;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "endpoint_data")
public class LogData {

    @ClusteringColumn
    @Column(name = "endpoint_id")
    private String endpointId;

    @Column(name = "data_type")
    private String dataType;

    @Column
    private Integer value;

    @PartitionKey
    @Column(name = "log_time")
    private Date logTime;

}
