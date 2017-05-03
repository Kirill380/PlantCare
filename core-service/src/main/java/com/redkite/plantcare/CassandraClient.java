package com.redkite.plantcare;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class CassandraClient {

  @Value("${cassandra.host}")
  private String host;

  @Value("${cassandra.keyspace}")
  private String keyspaceName;

  private Cluster cluster;
  private Session session;
  private MappingManager mappingManager;

  public Session getSession() {
    if (session == null) {
      session = cluster.connect(keyspaceName);
    }
    return session;
  }

  public <T> Mapper<T> getMapper(Class<T> clazz) {
    return getMappingManager().mapper(clazz);
  }

  private MappingManager getMappingManager() {
    if (mappingManager == null) {
      mappingManager = new MappingManager(getSession());
    }
    return mappingManager;
  }

  @PostConstruct
  public void init() {
    cluster = Cluster.builder()
              .addContactPoint(host)
              .build();
  }

  @PreDestroy
  public void  destroy() {
    if (cluster != null) {
      cluster.close();
    }
  }
}
