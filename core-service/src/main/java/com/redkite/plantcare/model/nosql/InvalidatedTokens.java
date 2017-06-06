package com.redkite.plantcare.model.nosql;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invalidated_tokens")
public class InvalidatedTokens {

  @PartitionKey
  @Column(name = "user_id")
  private Long userId;

  @Column
  private Long timestamp;
}
