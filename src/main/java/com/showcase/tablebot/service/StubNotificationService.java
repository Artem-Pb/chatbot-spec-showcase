package com.showcase.tablebot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.entity.TableBooking;
import com.showcase.tablebot.util.LogMessages;

@Component
@ConditionalOnMissingBean(TelegramNotificationService.class)
@Slf4j
public class StubNotificationService implements NotificationService {

    @Override
    public void notifyAdmin(TableBooking booking) {
        log.info(LogMessages.NOTIFICATION_ADMIN,
                booking.getId(),
                booking.getClientPhone(),
                booking.getTable().getName(),
                booking.getScheduledAt());
    }
}
