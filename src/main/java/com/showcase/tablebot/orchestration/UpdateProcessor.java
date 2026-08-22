package com.showcase.tablebot.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.dto.IncomingMessage;
import com.showcase.tablebot.dto.ProcessOutcome;
import com.showcase.tablebot.service.DialogService;
import com.showcase.tablebot.service.NotificationService;
import com.showcase.tablebot.util.LogMessages;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateProcessor {

    private final ProcessedUpdateGuard guard;
    private final DialogService dialogService;
    private final MessageDispatcher dispatcher;
    private final NotificationService notificationService;

    public void handle(IncomingMessage msg) {
        String value = msg.callbackData() != null ? msg.callbackData()
                : msg.text() != null ? msg.text()
                : msg.contactPhone();
        log.info(LogMessages.UPDATE_INCOMING, msg.platform(), msg.platformUserId(),
                msg.callbackData() != null ? "CALLBACK"
                        : msg.contactPhone() != null ? "CONTACT"
                        : msg.text() != null && msg.text().startsWith("/") ? "COMMAND" : "TEXT",
                value);

        if (guard.isDuplicate(msg.platform(), msg.externalId())) {
            log.info(LogMessages.UPDATE_DUPLICATE, msg.platform(), msg.externalId());
            return;
        }

        try {
            ProcessOutcome outcome = dialogService.process(msg);
            dispatcher.send(msg.platform(), outcome.reply());
            outcome.createdBooking().ifPresent(notificationService::notifyAdmin);
            log.info(LogMessages.UPDATE_DONE, msg.platform(), msg.platformUserId());
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn(LogMessages.UPDATE_OPTIMISTIC_LOCK, msg.platform(), msg.platformUserId());
        }
    }
}
