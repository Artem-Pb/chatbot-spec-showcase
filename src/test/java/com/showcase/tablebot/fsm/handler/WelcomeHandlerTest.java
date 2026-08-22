package com.showcase.tablebot.fsm.handler;

import com.showcase.tablebot.domain.entity.ChatUser;
import com.showcase.tablebot.domain.entity.UserSession;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Хендлер тестируется до перехода к следующему — WelcomeHandler простой:
 * проверяем, что он всегда переводит диалог в MENU_SELECTION и предлагает
 * ровно три кнопки выбора столика.
 */
class WelcomeHandlerTest {

    private final WelcomeHandler handler = new WelcomeHandler();

    @Test
    void handle_returnsMenuSelectionState_withThreeTableButtons() {
        ChatUser user = new ChatUser();
        user.setPlatform(Platform.TELEGRAM);
        user.setPlatformUserId("100");

        UserSession session = new UserSession();
        session.setState(DialogState.IDLE);

        DialogContext ctx = new DialogContext(null, session, user);

        HandlerResult result = handler.handle(ctx);

        assertThat(result.nextState()).isEqualTo(DialogState.MENU_SELECTION);
        assertThat(result.message().keyboard()).hasSize(3);
        assertThat(result.message().keyboard().get(0).get(0).callbackData()).isEqualTo("TABLE_1");
        assertThat(result.message().keyboard().get(1).get(0).callbackData()).isEqualTo("TABLE_2");
        assertThat(result.message().keyboard().get(2).get(0).callbackData()).isEqualTo("TABLE_3");
    }
}
