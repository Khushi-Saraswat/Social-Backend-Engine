package com.grid07.assignment.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.grid07.assignment.service.NotificationService;

@Component
public class NotificationScheduler {
    

    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    /*
          Runs every 5 minutes
    */

    @Scheduled(fixedRate = 30000)
    public void sweepNotifications(){
        System.out.println("Running scheduled task to process pending notifications...");
        notificationService.processPendingNotifications();
    }
}
