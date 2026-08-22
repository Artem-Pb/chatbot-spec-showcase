package com.showcase.tablebot.fsm.handler;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class SlotDateHandler implements DialogHandler {

    private static final int DAYS_AHEAD = 7;
    private static final DateTimeFormatter LABEL_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM (EEE)", Locale.forLanguageTag("ru"));

    @Override
    public DialogState handledState() {
        return DialogState.ZONE_MENU;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.SLOT_DATE_QUESTION,
                buildDateKeyboard()
        );
        return new HandlerResult(message, DialogState.SLOT_DATE_SELECTION);
    }

    private List<List<Button>> buildDateKeyboard() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<List<Button>> rows = new ArrayList<>();

        for (int i = 0; i < DAYS_AHEAD; i++) {
            LocalDate date = tomorrow.plusDays(i);
            rows.add(List.of(new Button(date.format(LABEL_FORMAT), "DATE_" + date)));
        }

        return rows;
    }
}
