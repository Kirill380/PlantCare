package com.redkite.plantcare.model.nosql;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.Data;

@Data
@Table(name = "invalidated_tokens")
public class InvalidatedTokens {

  @PartitionKey
  @Column(name = "user_id")
  private String userId;

  private Long timestamp;
}
