package com.airtribe.librarymanagement.pattern;

import java.util.*;
import java.util.logging.Logger;

public class NotificationService implements NotificationObserver {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    private Map<String, Queue<String>> notifications;

    public NotificationService() {
        this.notifications = new HashMap<>();
    }

    @Override
    public void update(String patronId, String message) {
        notifications.putIfAbsent(patronId, new LinkedList<>());
        notifications.get(patronId).offer(message);
        logger.info("Notification sent to patron " + patronId + ": " + message);
    }

    public List<String> getNotifications(String patronId) {
        Queue<String> patronNotifications = notifications.get(patronId);
        if (patronNotifications == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(patronNotifications);
    }

    public void clearNotifications(String patronId) {
        notifications.remove(patronId);
    }
}
