package com.showcase.tablebot.orchestration;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.platform.MessageSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MessageDispatcher {

    private final Map<Platform, MessageSender> byPlatform;

    public MessageDispatcher(List<MessageSender> senders) {
        this.byPlatform = senders.stream()
                .collect(Collectors.toMap(MessageSender::platform, Function.identity()));
    }

    public void send(Platform platform, OutgoingMessage message) {
        MessageSender sender = byPlatform.get(platform);

        if (sender == null) {
            throw new IllegalStateException("No MessageSender registered for platform: " + platform);
        }

        sender.send(message);
    }
}
