package com.redkite.plantcare.mocksensor;


import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.Uninterruptibles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Sensor {

  private static final String BASE_URL = "http://localhost:8083/api";

  private static final String LOG_DAT_URL = BASE_URL + "/sensors/data";

  private final Long sensorId;

  private final Integer frequency;

  private volatile boolean stop;


  public Sensor(Long sensorId, Integer frequency) {
    this.sensorId = sensorId;
    this.frequency = frequency;
  }


  public void launch() {
    new Thread(new SensorLogging()).start();
  }

  public void stop() {
    this.stop = true;
  }

  class SensorLogging implements Runnable {

    private CloseableHttpClient httpclient = HttpClients.createDefault();

    private ObjectMapper objectMapper = new ObjectMapper();

    private Generator generator = new Generator();

    @Override
    public void run() {
      while (true) {
        Uninterruptibles.sleepUninterruptibly(frequency, TimeUnit.SECONDS);
        HttpPost httpPost = new HttpPost(LOG_DAT_URL);
        httpPost.addHeader("Content-Type", "application/json");
        try {
          LogDataRequest logDataRequest = new LogDataRequest();
          logDataRequest.setDataType("moisture");
          logDataRequest.setSensorId(sensorId);
          logDataRequest.setValue(generator.next());
          httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(logDataRequest)));
          CloseableHttpResponse response = httpclient.execute(httpPost);
          String message = CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8));
          log.info("Status code: {}; Reason phrase: {}; Response message: {}; ",
                  response.getStatusLine().getStatusCode(),
                  response.getStatusLine().getReasonPhrase(),
                  message);
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        } finally {
          httpPost.releaseConnection();
        }

        if (stop) {
          try {
            httpclient.close();
          } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
          }
          break;
        }
      }
    }

  }


  private class Generator {

    private int step = 0;

    private int next() {
      if (step >= 200 || Math.random() > 0.9) {
        step = 0;
      }
      int value = (int) (80 / (Math.pow(Math.log10(step + 8), 2)) - 10 + 5 * Math.random());
      log.info("new value: " + value);
      step += 5;
      return value;
    }
  }
}
