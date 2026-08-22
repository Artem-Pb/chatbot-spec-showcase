package com.showcase.tablebot.platform.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.showcase.tablebot.util.LogMessages;

import java.util.Map;

@Slf4j
@Profile("!test")
@Component
@RequiredArgsConstructor
public class TelegramWebhookRegistrar implements ApplicationRunner {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.webhook-url}")
    private String webhookUrl;

    @Value("${telegram.webhook-secret}")
    private String webhookSecret;

    private final WebClient webClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String url = "https://api.telegram.org/bot" + botToken + "/setWebhook";
        webClient.post()
                .uri(url)
                .bodyValue(Map.of(
                        "url", webhookUrl,
                        "secret_token", webhookSecret
                ))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info(LogMessages.TG_WEBHOOK_OK, webhookUrl))
                .doOnError(e -> log.error(LogMessages.TG_WEBHOOK_ERROR, e.getMessage()))
                .block();
    }
}
