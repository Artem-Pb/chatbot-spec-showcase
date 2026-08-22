package com.showcase.tablebot.fsm.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.entity.SessionContext;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.dto.BookingProviderSlot;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;
import com.showcase.tablebot.service.BookingProviderClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SlotSelectionHandler implements DialogHandler {

    private static final int PAGE_SIZE = 5;

    private final BookingProviderClient bookingProviderClient;

    @Override
    public DialogState handledState() {
        return DialogState.SLOT_DATE_SELECTION;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        String callbackData = ctx.message().callbackData();
        SessionContext context = ctx.session().getContext();

        if (callbackData.startsWith("DATE_")) {
            LocalDate date = LocalDate.parse(callbackData.substring("DATE_".length()));
            context.setSelectedDate(date);
            context.setSlotPage(0);
        } else if ("NEXT_PAGE".equals(callbackData)) {
            context.setSlotPage(context.getSlotPage() + 1);
        } else if ("PREV_PAGE".equals(callbackData)) {
            context.setSlotPage(Math.max(0, context.getSlotPage() - 1));
        }

        String tableId = ctx.session().getSelectedTable().getBookingProviderTableId();
        List<BookingProviderSlot> allSlots =
                bookingProviderClient.getAvailableSlots(tableId, context.getSelectedDate());

        if (allSlots.isEmpty()) {
            return buildNoSlotsResult(ctx);
        }

        int from = context.getSlotPage() * PAGE_SIZE;
        if (from >= allSlots.size()) {
            context.setSlotPage(0);
            from = 0;
        }
        List<BookingProviderSlot> pageSlots =
                allSlots.subList(from, Math.min(from + PAGE_SIZE, allSlots.size()));

        context.setSlotOptions(pageSlots.stream()
                .map(s -> s.datetime().toString())
                .collect(Collectors.toList()));

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.SLOT_QUESTION,
                buildKeyboard(pageSlots, context.getSlotPage(), allSlots.size())
        );

        return new HandlerResult(message, DialogState.SLOT_SELECTION);
    }

    private List<List<Button>> buildKeyboard(List<BookingProviderSlot> slots, int page, int total) {
        List<List<Button>> rows = new ArrayList<>();
        for (int i = 0; i < slots.size(); i++) {
            rows.add(List.of(new Button(slots.get(i).time(), "SLOT_" + i)));
        }
        List<Button> nav = new ArrayList<>();
        if (page > 0) {
            nav.add(new Button(BotMessages.BTN_SLOT_PREV, "PREV_PAGE"));
        }
        if ((page + 1) * PAGE_SIZE < total) {
            nav.add(new Button(BotMessages.BTN_SLOT_NEXT, "NEXT_PAGE"));
        }
        if (!nav.isEmpty()) {
            rows.add(nav);
        }
        return rows;
    }

    private HandlerResult buildNoSlotsResult(DialogContext ctx) {
        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.SLOT_NO_SLOTS,
                List.of(List.of(new Button(BotMessages.BTN_BACK, "BACK")))
        );
        return new HandlerResult(message, DialogState.SLOT_DATE_SELECTION);
    }
}
