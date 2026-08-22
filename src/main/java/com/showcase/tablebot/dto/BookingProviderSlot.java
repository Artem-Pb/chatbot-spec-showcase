package com.showcase.tablebot.dto;

import java.time.LocalDateTime;

public record BookingProviderSlot(String time, LocalDateTime datetime, int durationSeconds) {
}
