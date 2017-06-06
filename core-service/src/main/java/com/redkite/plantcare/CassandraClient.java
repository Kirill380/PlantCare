package com.redkite.plantcare;

import com.google.common.io.CharStreams;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class CassandraClient {

  @Value("${cassandra.host}")
  private String host;

  @Value("${cassandra.keyspace}")
  private String keyspaceName;

  private static final String SEMICOLON = ";";

  private static final String HASH = "#";

  @Value("${cassandra.init-script}")
  private String initScript;

  private Cluster cluster;
  private Session session;
  private MappingManager mappingManager;

  /**
   * Get new session for working with cassandra.
   *
   * @return the session
   */
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
    cluster = Cluster.builder().addContactPoint(host).build();
    executeInitScript();
  }



  private void executeInitScript() {
    List<String> queries = readInitQueries();
    if (!queries.isEmpty()) {
      Session session = cluster.connect();
      for (String query : queries) {
        session.execute(query);
      }
      session.close();
    }
  }

  private List<String> readInitQueries() {
    String scripts;
    try {
      scripts = CharStreams
              .toString(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(initScript), "UTF-8"));
    } catch (IOException ex) {
      return Collections.emptyList();
    }

    if (!scripts.contains(SEMICOLON)) {
      log.warn("Cassandra init script does not contain any queries");
      return Collections.emptyList();
    }

    //remove comments
    scripts = scripts.replaceAll(HASH + ".+\n", "");

    List<String> queries = new ArrayList<>();
    int start = 0;
    int end = 0;
    while (true) {
      end = scripts.indexOf(SEMICOLON, start);
      if (end < 0) {
        break;
      }
      queries.add(scripts.substring(start, end + 1));
      start = end + 1;
    }
    return queries;
  }

  /**
   * Close connection with cassandra cluster.
   */
  @PreDestroy
  public void destroy() {
    if (cluster != null) {
      cluster.close();
    }
  }
}
