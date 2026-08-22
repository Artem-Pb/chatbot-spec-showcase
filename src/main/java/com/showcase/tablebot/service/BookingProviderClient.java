package com.showcase.tablebot.service;

import com.showcase.tablebot.dto.BookingRequest;
import com.showcase.tablebot.dto.BookingProviderResult;
import com.showcase.tablebot.dto.BookingProviderSlot;

import java.time.LocalDate;
import java.util.List;

/**
 * Абстракция над внешней системой бронирования (например, PMS/CRM ресторана).
 * Реальная реализация ходит во внешний API конкретного провайдера; для демо
 * можно подставить in-memory/stub-реализацию без реального провайдера.
 */
public interface BookingProviderClient {
    List<BookingProviderSlot> getAvailableSlots(String tableId, LocalDate date);
    BookingProviderResult book(BookingRequest req);
}
