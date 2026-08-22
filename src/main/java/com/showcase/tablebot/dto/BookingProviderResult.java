package com.showcase.tablebot.dto;

public record BookingProviderResult(boolean success,
                                    String reservationId,
                                    boolean slotTaken,
                                    String errorMessage) {
}
