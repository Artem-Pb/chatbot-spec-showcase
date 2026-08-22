package com.showcase.tablebot.dto;

import com.showcase.tablebot.domain.enums.EventType;

public record IncomingEvent(EventType type, String value) {
}
