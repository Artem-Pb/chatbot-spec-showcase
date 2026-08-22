package com.showcase.tablebot.fsm.handler;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.entity.RestaurantTable;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;

import java.util.List;

@Component
public class TableInfoHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return DialogState.ZONE_MENU;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        RestaurantTable table = ctx.session().getSelectedTable();
        if (table == null) {
            throw new IllegalStateException("No table in session for TableInfoHandler");
        }

        List<List<Button>> keyboard = List.of(
                List.of(new Button(BotMessages.BTN_WANT_BOOK, "WANT_BOOK")),
                List.of(new Button(BotMessages.BTN_BACK, "BACK"))
        );

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                formatTableInfo(table),
                keyboard
        );

        return new HandlerResult(message, DialogState.TABLE_INFO);
    }

    private String formatTableInfo(RestaurantTable table) {
        return table.getName() + "\n\n"
                + table.getDescription() + "\n\n"
                + "Мест: " + table.getCapacitySeats() + "\n"
                + "Стандартная длительность брони: " + table.getDefaultDurationMinutes() + " мин.";
    }
}
