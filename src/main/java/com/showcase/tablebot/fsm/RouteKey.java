package com.showcase.tablebot.fsm;

import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.EventType;

public record RouteKey(DialogState state, EventType eventType, String value) {

    public static RouteKey of(DialogState state, EventType type, String value) {
        return new RouteKey(state, type, value);
    }
}
