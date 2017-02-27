package com.redkite.plantcare.notifications.senders;


import com.redkite.plantcare.common.dto.NotificationRequest;

public interface NotificationSender {

  void sendNotification(NotificationRequest request);
}
