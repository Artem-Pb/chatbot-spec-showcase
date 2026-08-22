package com.showcase.tablebot.dto;

import com.showcase.tablebot.domain.entity.ChatUser;
import com.showcase.tablebot.domain.entity.UserSession;

public record DialogContext(IncomingMessage message,
                            UserSession session,
                            ChatUser user) {
}
