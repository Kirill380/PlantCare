package com.redkite.plantcare.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class Application {

  @Value("${sender.email:}")
  private String username;

  @Value("${sender.password:}")
  private String password;

  @Value("${sender.server.host}")
  private String host;

  @Value("${sender.server.port}")
  private String port;

  @Value("${sender.server.protocol}")
  private String protocol;

  //CHECKSTYLE:OFF
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.run(args);
  }

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    if(!username.isEmpty()) {
      javaMailSender.setUsername(username);
    }

    if(!password.isEmpty()) {
      javaMailSender.setPassword(password);
    }

    javaMailSender.setHost(host);
    javaMailSender.setPort(Integer.parseInt(port));
    javaMailSender.setProtocol(protocol);
    return javaMailSender;
  }
  //CHECKSTYLE:ON
}
