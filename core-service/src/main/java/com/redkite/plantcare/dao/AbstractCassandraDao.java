package com.redkite.plantcare.dao;


import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.redkite.plantcare.CassandraClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class AbstractCassandraDao<T, K> {

  @Autowired
  protected CassandraClient cassandraClient;

  private Session session;


  protected Session getSession() {
    if (session == null) {
      session = cassandraClient.getSession();
    }
    return session;
  }

  protected Mapper<T> getMapper() {
    return cassandraClient.getMapper(getColumnFamilyClass());
  }

  protected ConsistencyLevel getConsistencyLevel() {
    return ConsistencyLevel.ONE;
  }

  protected T save(T entity) {
    log.debug("Save entity " + entity);
    Statement saveStatement = getMapper().saveQuery(entity);
    saveStatement.setConsistencyLevel(getConsistencyLevel()); //TODO hardcoded
    getSession().execute(saveStatement);

    return entity;
  }


  public T find(K key) {
    return getMapper().get(key);
  }


  /**
   * Return all entities in table.
   *
   * @return the list of items.
   */
  public List<T> findAll() {
    return getMapper().map(
            getSession().execute(
                    QueryBuilder.select()
                            .all()
                            .from(getColumnFamilyName())
                            .setConsistencyLevel(getConsistencyLevel())
            )
    ).all();
  }

  public List<T> execute(Statement statement) {
    return getMapper().map(
            getSession().execute(statement)
    ).all();
  }

  protected void remove(K key) {
    getMapper().delete(key);
  }

  protected abstract Class<T> getColumnFamilyClass();

  protected abstract String getColumnFamilyName();


}
