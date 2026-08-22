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
public class ContactInputHandler implements DialogHandler {

    @Override
    public DialogState handledState() {
        return DialogState.SLOT_SELECTION;
    }

    @Override
    public HandlerResult handle(DialogContext ctx) {
        int slotIndex = parseSlotIndex(ctx.message().callbackData());
        String datetime = ctx.session().getContext().getSlotOptions().get(slotIndex);
        ctx.session().getContext().setSelectedDatetime(datetime);

        OutgoingMessage message = new OutgoingMessage(
                ctx.user().getPlatformUserId(),
                ctx.user().getPlatformGroupId(),
                BotMessages.CONTACT_QUESTION,
                List.of(List.of(new Button(BotMessages.BTN_SHARE_CONTACT, "SHARE_CONTACT")))
        );

        return new HandlerResult(message, DialogState.CONTACT_INPUT);
    }

    private int parseSlotIndex(String callbackData) {
        return Integer.parseInt(callbackData.substring("SLOT_".length()));
    }
}
