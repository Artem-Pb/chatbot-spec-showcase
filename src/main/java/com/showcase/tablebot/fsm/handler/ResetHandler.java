package com.showcase.tablebot.fsm.handler;

import com.showcase.tablebot.domain.entity.SessionContext;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;
import com.showcase.tablebot.fsm.BotMessages;
import com.showcase.tablebot.fsm.DialogHandler;

@Component
public class ResetHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return null;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        ctx.session().setContext(new SessionContext());
        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.RESET_DONE,
                null
        );

        return new HandlerResult(message, DialogState.IDLE);
    }
}
