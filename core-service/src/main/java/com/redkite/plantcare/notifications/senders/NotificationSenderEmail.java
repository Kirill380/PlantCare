package com.redkite.plantcare.notifications.senders;

import com.redkite.plantcare.common.dto.NotificationRequest;
import com.redkite.plantcare.common.dto.NotificationType;
import com.redkite.plantcare.notifications.MessageType;
import com.redkite.plantcare.notifications.NotificationException;

import freemarker.template.Configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Slf4j
@Component
public class NotificationSenderEmail implements NotificationSender {

  @Autowired
  Configuration freemarkerConfiguration;

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Override
  public void sendNotification(NotificationRequest request) {
    MimeMessagePreparator preparator = mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setFrom(senderEmail);
      message.setTo(request.getTargetEmail());
      MessageType messageType = getMessageType(request.getType());
      message.setSubject(messageType.getSubject());
      String content = composeContent(messageType.getTemplateFile(), request.getEmailProperties());
      message.setText(content, true);
    };
    mailSender.send(preparator);
    log.info("Sent {} notification to user with email [{}]", request.getType(), request.getTargetEmail());
  }

  private MessageType getMessageType(NotificationType type) {
    switch (type) {
      case USER_ACTIVATION:
        return MessageType.ACTIVATION;
      default:
        log.error("Unexpected notification type: " + type);
        throw new NotificationException("Unexpected notification type: " + type, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String composeContent(String templateName, Map<String, String> model) {
    try {
      return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), model);
    } catch (Exception ex) {
      log.error("The error occurred during composing message:", ex);
      throw new NotificationException("The error occurred during composing message: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
