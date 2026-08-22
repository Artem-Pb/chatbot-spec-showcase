package com.showcase.tablebot.platform.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.platform.MessageSender;
import com.showcase.tablebot.util.LogMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramSender implements MessageSender {

    @Value("${telegram.bot-token}")
    private String botToken;

    private final WebClient webClient;

    @Override
    public Platform platform() {
        return Platform.TELEGRAM;
    }

    @Override
    public void send(OutgoingMessage message) {
        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", message.recipientId());
        body.put("text", message.text());

        if (message.keyboard() != null && !message.keyboard().isEmpty()) {
            body.put("reply_markup", buildReplyMarkup(message.keyboard()));
        }

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .doOnError(e -> log.error(LogMessages.TG_SEND_ERROR,
                        message.recipientId(), e.getMessage()))
                .subscribe();
    }

    private Map<String, Object> buildReplyMarkup(List<List<Button>> keyboard) {
        boolean isContactRequest = keyboard.stream()
                .flatMap(List::stream)
                .anyMatch(b -> b.callbackData() == null);

        if (isContactRequest) {
            List<List<Map<String, Object>>> replyKeyboard = keyboard.stream()
                    .map(row -> row.stream()
                            .map(b -> Map.<String, Object>of(
                                    "text", b.label(),
                                    "request_contact", true))
                            .collect(Collectors.toList()))
                    .toList();

            return Map.of(
                    "keyboard", replyKeyboard,
                    "resize_keyboard", true,
                    "one_time_keyboard", true
            );
        }

        List<List<Map<String, String>>> inlineKeyboard = keyboard.stream()
                .map(row -> row.stream()
                        .map(b -> Map.of("text", b.label(), "callback_data", b.callbackData()))
                        .collect(Collectors.toList()))
                .toList();
        return Map.of("inline_keyboard", inlineKeyboard);
    }
}
