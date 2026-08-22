package com.showcase.tablebot.fsm.handler;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.Button;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;

import java.util.List;

@Component
public class ConsultationHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return DialogState.ZONE_MENU;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        List<List<Button>> keyboard = List.of(
                List.of(new Button(BotMessages.BTN_WANT_BOOK, "WANT_BOOK")),
                List.of(new Button(BotMessages.BTN_BACK, "BACK"))
        );

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.CONSULTATION_INFO,
                keyboard
        );

        return new HandlerResult(message, DialogState.CONSULTATION_INFO);
    }
}
