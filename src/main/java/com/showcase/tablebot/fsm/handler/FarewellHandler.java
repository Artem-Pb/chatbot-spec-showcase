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
public class FarewellHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return null;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        List<List<Button>> keyboard = List.of(
                List.of(new Button(BotMessages.BTN_START_OVER, "START_OVER"))
        );

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.FAREWELL,
                keyboard
        );

        return new HandlerResult(message, DialogState.IDLE);
    }
}
