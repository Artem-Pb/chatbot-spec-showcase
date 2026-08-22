package com.showcase.tablebot.dto;

import com.showcase.tablebot.domain.enums.Platform;

public record IncomingMessage(Platform platform,
                              String platformUserId,
                              String platformGroupId,
                              String externalId,
                              String callbackId,
                              String text,
                              String callbackData,
                              String contactPhone,
                              String contactName,
                              String rawPayload) {
}
