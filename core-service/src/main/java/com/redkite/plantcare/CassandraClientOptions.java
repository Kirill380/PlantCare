package com.redkite.plantcare;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CassandraClientOptions {

  /* Socket parameters */
  @Value("#{cassandra_properties[socket_connect_timeout]}")
  private int connectTimeoutMillis;

  @Value("#{cassandra_properties[socket_read_timeout]}")
  private int readTimeoutMillis;

  @Value("#{cassandra_properties[socket_keep_alive]}")
  private Boolean keepAlive;

  @Value("#{cassandra_properties[socket_reuse_address]}")
  private Boolean reuseAddress;

  @Value("#{cassandra_properties[socket_so_linger]}")
  private Integer soLinger;

  @Value("#{cassandra_properties[socket_tcp_no_delay]}")
  private Boolean tcpNoDelay;

  @Value("#{cassandra_properties[socket_receive_buffer_size]}")
  private Integer receiveBufferSize;

  @Value("#{cassandra_properties[socket_send_buffer_size]}")
  private Integer sendBufferSize;

  private SocketOptions socketOptions;

  /* Query parameters */
  @Value("ONE")
  private String consistencyLevel;

  @Value("2000")
  private Integer defaultFetchSize;

  private QueryOptions queryOptions;

  /**
   * Get value of field queryOptions. If it's null, than create new instance and assign on this
   * field.
   *
   * @return queryOptions
   */
  public QueryOptions getQueryOptions() {
    if (queryOptions == null) {
      queryOptions = new QueryOptions();
      queryOptions.setConsistencyLevel(parseConsistencyLevel(consistencyLevel));
      queryOptions.setFetchSize(defaultFetchSize);
    }
    return queryOptions;
  }

  public void setQueryOptions(QueryOptions queryOptions) {
    this.queryOptions = queryOptions;
  }

  /**
   * Getter of socketOptions. If this field is null, than create new instance of
   * <code>SocketOptions</code> and set default values.
   *
   * @return value of field <code>socketOptions</code>
   */
  public SocketOptions getSocketOptions() {
    if (socketOptions == null) {
      socketOptions = new SocketOptions();
      socketOptions.setConnectTimeoutMillis(connectTimeoutMillis);
      socketOptions.setReadTimeoutMillis(readTimeoutMillis);
      if (keepAlive != null) {
        socketOptions.setKeepAlive(keepAlive);
      }

      if (reuseAddress != null) {
        socketOptions.setReuseAddress(reuseAddress);
      }

      if (soLinger != null) {
        socketOptions.setSoLinger(soLinger);
      }

      if (tcpNoDelay != null) {
        socketOptions.setTcpNoDelay(tcpNoDelay);
      }

      if (receiveBufferSize != null) {
        socketOptions.setReceiveBufferSize(receiveBufferSize);
      }

      if (sendBufferSize != null) {
        socketOptions.setSendBufferSize(sendBufferSize);
      }
    }
    return socketOptions;
  }

  public void setSocketOptions(SocketOptions socketOptions) {
    this.socketOptions = socketOptions;
  }

  private ConsistencyLevel parseConsistencyLevel(String level) {
    ConsistencyLevel consistencyLevel = ConsistencyLevel.ANY;
    if (!isBlank(level)) {
      for (ConsistencyLevel current : ConsistencyLevel.values()) {
        if (current.name().equalsIgnoreCase(level)) {
          consistencyLevel = current;
          break;
        }
      }
    }
    return consistencyLevel;
  }


}