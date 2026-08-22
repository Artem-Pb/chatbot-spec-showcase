package com.showcase.tablebot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.showcase.tablebot.domain.entity.TableBooking;
import com.showcase.tablebot.util.LogMessages;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.admin-chat-id}")
    private String adminChatId;

    @Value("${telegram.api-base-url}")
    private String apiBaseUrl;

    private final WebClient webClient;

    @Override
    public void notifyAdmin(TableBooking booking) {
        String text = buildText(booking);

        webClient.post()
                .uri(apiBaseUrl + "/bot" + botToken + "/sendMessage")
                .bodyValue(new SendMessageRequest(adminChatId, text))
                .retrieve()
                .toBodilessEntity()
                .doOnError(e -> log.error(LogMessages.TG_SEND_ERROR, adminChatId, e.getMessage()))
                .subscribe();

        log.info(LogMessages.NOTIFICATION_ADMIN,
                booking.getId(),
                booking.getClientPhone(),
                booking.getTable().getName(),
                booking.getScheduledAt());
    }

    private String buildText(TableBooking booking) {
        return "Новая бронь #" + booking.getId() + "\n"
                + "Платформа: " + booking.getPlatform() + "\n"
                + "Столик: " + booking.getTable().getName() + "\n"
                + "Имя: " + nullSafe(booking.getClientName()) + "\n"
                + "Телефон: " + nullSafe(booking.getClientPhone());
    }

    private String nullSafe(String value) {
        return value != null ? value : "—";
    }

    record SendMessageRequest(String chat_id, String text) {}
}
