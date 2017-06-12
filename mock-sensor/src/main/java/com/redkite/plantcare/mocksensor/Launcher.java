package com.redkite.plantcare.mocksensor;


import java.io.IOException;

public class Launcher {

  public static void main(String[] args) throws IOException {

    if (args.length < 1) {
      throw new IllegalArgumentException("Sensor id is not specified");
    }

    Long sensorId = Long.parseLong(args[0]);
    Integer frequency = args.length == 2 ? Integer.parseInt(args[1]) : 15;

    Sensor sensor = new Sensor(sensorId, frequency);

    sensor.launch();

    System.in.read();

    sensor.stop();
  }

}
