package com.showcase.tablebot.fsm.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.entity.TableBooking;
import com.showcase.tablebot.domain.entity.SessionContext;
import com.showcase.tablebot.domain.enums.BookingStatus;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.*;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;
import com.showcase.tablebot.repository.TableBookingRepository;
import com.showcase.tablebot.service.BookingProviderClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingHandler implements DialogHandler {

    private static final int PAGE_SIZE = 5;

    private final BookingProviderClient bookingProviderClient;
    private final TableBookingRepository tableBookingRepository;

    @Override
    public DialogState handledState() {
        return DialogState.CONTACT_INPUT;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        SessionContext context = ctx.session().getContext();

        String phone = extractPhone(ctx.message());
        String name = extractName(ctx.message());
        context.setClientPhone(phone);
        context.setClientName(name);

        BookingRequest request = new BookingRequest(
                ctx.session().getSelectedTable().getBookingProviderTableId(),
                LocalDateTime.parse(context.getSelectedDatetime()),
                phone,
                name
        );

        BookingProviderResult result = bookingProviderClient.book(request);

        if (result.slotTaken()) {
            return handleConflict(ctx, context);
        }

        TableBooking booking = buildBooking(ctx, result.reservationId());
        tableBookingRepository.save(booking);

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.BOOKING_SUCCESS,
                null
        );

        return new HandlerResult(message, DialogState.AWAITING_CONFIRMATION, Optional.of(booking));
    }

    private HandlerResult handleConflict(DialogContext ctx, SessionContext context) {
        List<BookingProviderSlot> fresh = bookingProviderClient.getAvailableSlots(
                ctx.session().getSelectedTable().getBookingProviderTableId(),
                context.getSelectedDate()
        );

        if (fresh.isEmpty()) {
            OutgoingMessage msg = new OutgoingMessage(
                    ctx.user().getPlatformUserId(),
                    ctx.user().getPlatformGroupId(),
                    BotMessages.SLOT_NO_SLOTS,
                    List.of(List.of(new Button(BotMessages.BTN_BACK, "BACK")))
            );
            return new HandlerResult(msg, DialogState.SLOT_DATE_SELECTION);
        }

        context.setSlotPage(0);
        List<BookingProviderSlot> page = fresh.subList(0, Math.min(PAGE_SIZE, fresh.size()));
        context.setSlotOptions(page.stream()
                .map(s -> s.datetime().toString())
                .collect(Collectors.toList()));

        List<List<Button>> keyboard = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            keyboard.add(List.of(new Button(page.get(i).time(), "SLOT_" + i)));
        }
        if (fresh.size() > PAGE_SIZE) {
            keyboard.add(List.of(new Button(BotMessages.BTN_SLOT_NEXT, "NEXT_PAGE")));
        }

        OutgoingMessage msg = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.BOOKING_CONFLICT,
                keyboard
        );
        return new HandlerResult(msg, DialogState.SLOT_SELECTION);
    }

    private TableBooking buildBooking(DialogContext ctx, String reservationId) {
        SessionContext context = ctx.session().getContext();
        TableBooking b = new TableBooking();
        b.setChatUser(ctx.session().getChatUser());
        b.setTable(ctx.session().getSelectedTable());
        b.setPlatform(ctx.message().platform());
        b.setPlatformGroupId(ctx.message().platformGroupId());
        b.setClientName(context.getClientName());
        b.setClientPhone(context.getClientPhone());
        b.setScheduledAt(LocalDateTime.parse(context.getSelectedDatetime()));
        b.setBookingProviderReservationId(reservationId);
        b.setStatus(BookingStatus.AUTO_BOOKED);
        b.setCreatedAt(LocalDateTime.now());
        return b;
    }

    private String extractPhone(IncomingMessage msg) {
        if (msg.contactPhone() != null) return msg.contactPhone();
        if (msg.text() == null) return "";

        String[] parts = msg.text().trim().split("\\s+");
        for (int i = parts.length - 1; i >= 0; i--) {
            String cleaned = parts[i].replaceAll("[()\\-]", "");
            if (cleaned.matches("\\+?[78]\\d{10}")) {
                return cleaned.replaceFirst("^\\+?8", "7");
            }
        }

        String digits = msg.text().replaceAll("[^\\d+]", "");
        if (digits.matches("\\+?[78]\\d{10}")) {
            return digits.replaceFirst("^\\+?8", "7");
        }

        return msg.text().trim();
    }

    private String extractName(IncomingMessage msg) {
        if (msg.contactName() != null) return msg.contactName();
        if (msg.text() == null) return "Гость";
        String[] parts = msg.text().trim().split("\\s+");
        if (parts.length <= 1) return "Гость";
        return String.join(" ", Arrays.copyOf(parts, parts.length - 1));
    }
}
