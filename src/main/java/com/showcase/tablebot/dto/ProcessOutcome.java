package com.showcase.tablebot.dto;

import com.showcase.tablebot.domain.entity.TableBooking;

import java.util.Optional;

public record ProcessOutcome(OutgoingMessage reply,
                             Optional<TableBooking> createdBooking) {
}
