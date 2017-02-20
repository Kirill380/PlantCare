package com.redkite.plantcare.notifications;


public enum MessageType {
  ACTIVATION("Plant care: Account activation", "activation_letter.ftl");

  private final String subject;

  private final String templateFile;


  MessageType(String subject, String templateFile) {
    this.subject = subject;
    this.templateFile = templateFile;
  }

  public String getSubject() {
    return subject;
  }

  public String getTemplateFile() {
    return templateFile;
  }
}
