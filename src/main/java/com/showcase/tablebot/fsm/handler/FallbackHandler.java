package com.showcase.tablebot.fsm.handler;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;

@Component
public class FallbackHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return null;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.FALLBACK,
                null
        );

        return new HandlerResult(message, ctx.session().getState());
    }
}
