package com.redkite.plantcare.model.nosql;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.Data;

@Data
@Table(name = "token_blacklist")
public class BlackListedToken {

  @PartitionKey
  private String token;
}
