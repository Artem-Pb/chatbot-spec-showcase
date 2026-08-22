package com.showcase.tablebot.fsm;

import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;

public interface DialogHandler {

    DialogState handledState();
    HandlerResult handle(DialogContext ctx);
}
