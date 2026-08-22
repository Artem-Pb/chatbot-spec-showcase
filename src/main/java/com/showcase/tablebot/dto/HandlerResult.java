package com.showcase.tablebot.dto;

import com.showcase.tablebot.domain.entity.TableBooking;
import com.showcase.tablebot.domain.enums.DialogState;

import java.util.Optional;

public record HandlerResult(OutgoingMessage message, DialogState nextState, Optional<TableBooking> booking) {

    public HandlerResult(OutgoingMessage message, DialogState nextState) {
        this(message, nextState, Optional.empty());
    }
}
