package com.showcase.tablebot.platform.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.IncomingMessage;

@Component
public class TelegramNormalizer {

    public IncomingMessage normalize(JsonNode body) {
        String updateId = body.path("update_id").asText();
        String raw = body.toString();

        if (body.has("callback_query")) {
            JsonNode cq = body.get("callback_query");
            return new IncomingMessage(
                    Platform.TELEGRAM,
                    cq.path("from").path("id").asText(),
                    cq.path("message").path("chat").path("id").asText(),
                    updateId,
                    cq.path("id").asText(),
                    null,
                    cq.path("data").asText(null),
                    null,
                    null,
                    raw
            );
        }

        if (body.has("message")) {
            JsonNode msg = body.get("message");
            String userId = msg.path("from").path("id").asText();
            String chatId = msg.path("chat").path("id").asText();

            if (msg.has("contact")) {
                JsonNode contact = msg.get("contact");
                return new IncomingMessage(
                        Platform.TELEGRAM,
                        userId,
                        chatId,
                        updateId,
                        null,
                        null,
                        null,
                        contact.path("phone_number").asText(null),
                        contact.path("first_name").asText(null),
                        raw
                );
            }

            if (msg.has("text")) {
                return new IncomingMessage(
                        Platform.TELEGRAM,
                        userId,
                        chatId,
                        updateId,
                        null,
                        msg.path("text").asText(null),
                        null,
                        null,
                        null,
                        raw
                );
            }
        }

        return null;
    }
}
