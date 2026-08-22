package com.showcase.tablebot.fsm.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.entity.RestaurantTable;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;
import com.showcase.tablebot.repository.RestaurantTableRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ZoneMenuHandler implements DialogHandler {

    private final RestaurantTableRepository restaurantTableRepository;

    @Override
    public DialogState handledState() {
        return DialogState.MENU_SELECTION;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        String callbackData = ctx.message().callbackData();

        long tableId = Long.parseLong(callbackData.substring("TABLE_".length()));
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalStateException("Table not found: " + tableId));
        ctx.session().setSelectedTable(table);

        List<List<Button>> keyboard = List.of(
                List.of(new Button(BotMessages.BTN_ZONE_INFO, "INFO")),
                List.of(new Button(BotMessages.BTN_ZONE_ASK, "ASK")),
                List.of(new Button(BotMessages.BTN_ZONE_BOOK, "BOOK")),
                List.of(new Button(BotMessages.BTN_ZONE_FAQ, "FAQ")),
                List.of(new Button(BotMessages.BTN_BACK, "BACK"))
        );

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                table.getName() + "\n\n" + BotMessages.ZONE_MENU_INTRO,
                keyboard
        );

        return new HandlerResult(message, DialogState.ZONE_MENU);
    }
}
