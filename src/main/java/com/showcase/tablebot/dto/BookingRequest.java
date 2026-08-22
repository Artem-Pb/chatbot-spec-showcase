package com.showcase.tablebot.dto;

import java.time.LocalDateTime;

public record BookingRequest(String tableId,
                             LocalDateTime datetime,
                             String phone,
                             String fullName) {
}
