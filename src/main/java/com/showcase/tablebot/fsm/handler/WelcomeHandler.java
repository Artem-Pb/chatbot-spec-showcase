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
public class WelcomeHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return DialogState.IDLE;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        List<List<Button>> keyboard = List.of(
                List.of(new Button(BotMessages.BTN_TABLE_1, "TABLE_1")),
                List.of(new Button(BotMessages.BTN_TABLE_2, "TABLE_2")),
                List.of(new Button(BotMessages.BTN_TABLE_3, "TABLE_3"))
        );

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.WELCOME,
                keyboard
        );

        return new HandlerResult(message, DialogState.MENU_SELECTION);
    }
}
