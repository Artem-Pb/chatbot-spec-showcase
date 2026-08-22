package com.showcase.tablebot.fsm;

import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.EventType;
import com.showcase.tablebot.dto.DialogContext;
import com.showcase.tablebot.dto.HandlerResult;
import com.showcase.tablebot.dto.IncomingEvent;
import com.showcase.tablebot.fsm.handler.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Юнит-тест на саму механику маршрутизации DialogRouter: точное совпадение,
 * префиксное совпадение, глобальные команды, откат к fallback. Хендлеры —
 * простые заглушки, реальная бизнес-логика хендлеров здесь не участвует.
 */
class DialogRouterTest {

    private DialogRouter router;
    private WelcomeHandler welcomeHandler;
    private ZoneMenuHandler zoneMenuHandler;
    private FallbackHandler fallbackHandler;
    private ResetHandler resetHandler;

    @BeforeEach
    void setUp() {
        welcomeHandler = mock(WelcomeHandler.class);
        zoneMenuHandler = mock(ZoneMenuHandler.class);
        fallbackHandler = mock(FallbackHandler.class);
        resetHandler = mock(ResetHandler.class);

        router = new DialogRouter(
                welcomeHandler,
                zoneMenuHandler,
                mock(TableInfoHandler.class),
                mock(ConsultationHandler.class),
                mock(FaqHandler.class),
                mock(SlotDateHandler.class),
                mock(SlotSelectionHandler.class),
                mock(ContactInputHandler.class),
                mock(BookingHandler.class),
                mock(FarewellHandler.class),
                resetHandler,
                fallbackHandler
        );
    }

    @Test
    void idleState_anyEvent_resolvesToWelcomeHandler() {
        var handler = router.resolve(DialogState.IDLE, new IncomingEvent(EventType.TEXT, "hello"));
        assertThat(handler).isSameAs(welcomeHandler);
    }

    @Test
    void menuSelection_exactTableCallback_resolvesByPrefix() {
        // "TABLE_2" не зарегистрирован явно — должен сработать префикс "TABLE_"
        var handler = router.resolve(DialogState.MENU_SELECTION,
                new IncomingEvent(EventType.CALLBACK, "TABLE_2"));
        assertThat(handler).isSameAs(zoneMenuHandler);
    }

    @Test
    void globalCommand_resetWorksFromAnyState() {
        // /reset зарегистрирован глобально (state=null) — должен резолвиться
        // независимо от текущего состояния диалога.
        var handler = router.resolve(DialogState.SLOT_SELECTION,
                new IncomingEvent(EventType.COMMAND, "/reset"));
        assertThat(handler).isSameAs(resetHandler);
    }

    @Test
    void unknownCallbackInKnownState_fallsBackToFallbackHandler() {
        var handler = router.resolve(DialogState.FAQ_VIEWING,
                new IncomingEvent(EventType.CALLBACK, "UNKNOWN_BUTTON"));
        assertThat(handler).isSameAs(fallbackHandler);
    }
}
