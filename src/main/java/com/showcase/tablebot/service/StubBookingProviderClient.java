package com.showcase.tablebot.service;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.dto.BookingRequest;
import com.showcase.tablebot.dto.BookingProviderResult;
import com.showcase.tablebot.dto.BookingProviderSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Демонстрационная реализация без реального внешнего провайдера: генерирует
 * слоты каждые 30 минут в рабочем окне и всегда подтверждает бронь. Для
 * реального провайдера — заменить бином, реализующим {@link BookingProviderClient}.
 */
@Component
public class StubBookingProviderClient implements BookingProviderClient {

    private static final LocalTime OPEN = LocalTime.of(12, 0);
    private static final LocalTime CLOSE = LocalTime.of(22, 0);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<BookingProviderSlot> getAvailableSlots(String tableId, LocalDate date) {
        List<BookingProviderSlot> slots = new ArrayList<>();
        LocalTime cursor = OPEN;
        while (cursor.isBefore(CLOSE)) {
            LocalDateTime dt = LocalDateTime.of(date, cursor);
            slots.add(new BookingProviderSlot(cursor.format(TIME_FORMAT), dt, 90 * 60));
            cursor = cursor.plusMinutes(30);
        }
        return slots;
    }

    @Override
    public BookingProviderResult book(BookingRequest req) {
        return new BookingProviderResult(true, "demo-" + req.datetime(), false, null);
    }
}
