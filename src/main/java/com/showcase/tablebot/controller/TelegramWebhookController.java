package com.showcase.tablebot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.bind.annotation.PostMapping;
import com.showcase.tablebot.orchestration.UpdateProcessor;
import com.showcase.tablebot.platform.telegram.TelegramNormalizer;
import com.showcase.tablebot.util.LogMessages;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TelegramWebhookController {

    @Value("${telegram.webhook-secret}")
    private String webhookSecret;

    @Value("${telegram.bot-token}")
    private String botToken;

    private final UpdateProcessor updateProcessor;
    private final TelegramNormalizer normalizer;
    private final WebClient webClient;


    @PostMapping("/webhook/telegram")
    public ResponseEntity<Void> handle(
            @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String secret,
            @RequestBody JsonNode body) {

        if (!webhookSecret.equals(secret)) {
            log.warn(LogMessages.TG_INVALID_SECRET);
            return ResponseEntity.status(403).build();
        }

        if (body.has("callback_query")) {
            String callbackId = body.get("callback_query").get("id").asText();
            answerCallbackQuery(callbackId);
        }

        var incoming = normalizer.normalize(body);
        if (incoming == null) {
            return ResponseEntity.ok().build();
        }

        updateProcessor.handle(incoming);
        return ResponseEntity.ok().build();
    }

    private void answerCallbackQuery(String callbackId) {
        String url = "https://api.telegram.org/bot" + botToken + "/answerCallbackQuery";
        webClient.post()
                .uri(url)
                .bodyValue(Map.of("callback_query_id", callbackId))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
