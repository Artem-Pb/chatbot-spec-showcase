package com.showcase.tablebot.platform;

import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.OutgoingMessage;

public interface MessageSender {

    Platform platform();

    void send(OutgoingMessage message);
}
