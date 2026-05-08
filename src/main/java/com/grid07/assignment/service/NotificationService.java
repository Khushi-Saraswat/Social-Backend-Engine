package com.grid07.assignment.service;
import java.util.Set;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final StringRedisTemplate redisTemplate;

    public NotificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void handleBotNotification(Long userId, String message) {

        String cooldownKey = "notif:cooldown:user:" + userId;

        Boolean exists = redisTemplate.hasKey(cooldownKey);

        if (Boolean.TRUE.equals(exists)) {

            String pendingKey = "user:" + userId + ":pending_notifs";
            redisTemplate.opsForList().rightPush(pendingKey, message);

            System.out.println("User " + userId + " is in cooldown. Notification queued.");

        } else {

            System.out.println("Sending notification to user " + userId + ": " + message);

            redisTemplate.opsForValue()
                    .set(cooldownKey, "sent", java.time.Duration.ofMinutes(15));
        }
    }

    public Set<String> getPendingNotificationsKeys() {
        return redisTemplate.keys("user:*:pending_notifs");
    }

    public void processPendingNotifications() {

        Set<String> keys = getPendingNotificationsKeys();

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {

            java.util.List<String> messages =
                    redisTemplate.opsForList().range(key, 0, -1);

            if (messages == null || messages.isEmpty()) continue;

            int total = messages.size();
            String first = messages.get(0);

            System.out.println(
                    "Summarized Push Notification: "
                            + first
                            + " and "
                            + (total - 1)
                            + " others interacted with your posts."
            );

            redisTemplate.delete(key);
        }
    }
}
