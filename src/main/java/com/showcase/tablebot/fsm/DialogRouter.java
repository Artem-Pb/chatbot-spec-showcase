package com.showcase.tablebot.fsm;

import org.springframework.stereotype.Component;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.EventType;
import com.showcase.tablebot.dto.IncomingEvent;
import com.showcase.tablebot.fsm.handler.*;

import java.util.HashMap;
import java.util.Map;

import static com.showcase.tablebot.domain.enums.DialogState.*;
import static com.showcase.tablebot.domain.enums.EventType.*;

@Component
public class DialogRouter {

    private final Map<RouteKey, DialogHandler> routes = new HashMap<>();
    private final DialogHandler fallbackHandler;

    public DialogRouter(
            WelcomeHandler welcomeHandler,
            ZoneMenuHandler zoneMenuHandler,
            TableInfoHandler tableInfoHandler,
            ConsultationHandler consultationHandler,
            FaqHandler faqHandler,
            SlotDateHandler slotDateHandler,
            SlotSelectionHandler slotSelectionHandler,
            ContactInputHandler contactInputHandler,
            BookingHandler bookingHandler,
            FarewellHandler farewellHandler,
            ResetHandler resetHandler,
            FallbackHandler fallbackHandler
    ) {
        this.fallbackHandler = fallbackHandler;

        // IDLE — любое событие
        routes.put(RouteKey.of(IDLE, null, null), welcomeHandler);

        // MENU_SELECTION — выбор зоны/столика (префикс "TABLE_" ловит TABLE_1/2/3 и далее)
        routes.put(RouteKey.of(MENU_SELECTION, CALLBACK, "TABLE_"), zoneMenuHandler);

        // ZONE_MENU — меню конкретного столика
        routes.put(RouteKey.of(ZONE_MENU, CALLBACK, "INFO"), tableInfoHandler);
        routes.put(RouteKey.of(ZONE_MENU, CALLBACK, "ASK"), consultationHandler);
        routes.put(RouteKey.of(ZONE_MENU, CALLBACK, "BOOK"), slotDateHandler);
        routes.put(RouteKey.of(ZONE_MENU, CALLBACK, "FAQ"), faqHandler);
        routes.put(RouteKey.of(ZONE_MENU, CALLBACK, "BACK"), welcomeHandler);

        // TABLE_INFO
        routes.put(RouteKey.of(TABLE_INFO, CALLBACK, "WANT_BOOK"), slotDateHandler);
        routes.put(RouteKey.of(TABLE_INFO, CALLBACK, "BACK"), welcomeHandler);

        // CONSULTATION_INFO
        routes.put(RouteKey.of(CONSULTATION_INFO, CALLBACK, "WANT_BOOK"), slotDateHandler);
        routes.put(RouteKey.of(CONSULTATION_INFO, CALLBACK, "BACK"), welcomeHandler);

        // FAQ_VIEWING
        routes.put(RouteKey.of(FAQ_VIEWING, CALLBACK, "BACK"), welcomeHandler);

        // SLOT_DATE_SELECTION / SLOT_SELECTION — выбор даты и времени брони
        routes.put(RouteKey.of(SLOT_DATE_SELECTION, CALLBACK, "DATE_"), slotSelectionHandler);
        routes.put(RouteKey.of(SLOT_DATE_SELECTION, CALLBACK, "BACK"), welcomeHandler);

        routes.put(RouteKey.of(SLOT_SELECTION, CALLBACK, "NEXT_PAGE"), slotSelectionHandler);
        routes.put(RouteKey.of(SLOT_SELECTION, CALLBACK, "PREV_PAGE"), slotSelectionHandler);
        routes.put(RouteKey.of(SLOT_SELECTION, CALLBACK, "SLOT_"), contactInputHandler);

        // CONTACT_INPUT — сбор контакта, завершение брони
        routes.put(RouteKey.of(CONTACT_INPUT, CONTACT, null), bookingHandler);
        routes.put(RouteKey.of(CONTACT_INPUT, TEXT, null), bookingHandler);

        // AWAITING_CONFIRMATION
        routes.put(RouteKey.of(AWAITING_CONFIRMATION, CALLBACK, "TO_MENU"), welcomeHandler);
        routes.put(RouteKey.of(AWAITING_CONFIRMATION, CALLBACK, "FAREWELL"), farewellHandler);

        // Глобальные команды
        routes.put(RouteKey.of(null, COMMAND, "/start"), welcomeHandler);
        routes.put(RouteKey.of(null, COMMAND, "/menu"), welcomeHandler);
        routes.put(RouteKey.of(null, COMMAND, "/reset"), resetHandler);
    }

    public DialogHandler resolve(DialogState state, IncomingEvent event) {
        String value = event.value();
        EventType type = event.type();

        // 1. Точное совпадение
        DialogHandler handler = routes.get(RouteKey.of(state, type, value));
        if (handler != null) return handler;

        // 2. Префиксное совпадение: "TABLE_1" → "TABLE_", "DATE_2026-09-01" → "DATE_"
        if (value != null && value.contains("_")) {
            String prefix = value.substring(0, value.indexOf('_') + 1);
            handler = routes.get(RouteKey.of(state, type, prefix));
            if (handler != null) return handler;
        }

        // 3. Глобальные команды из любого состояния
        if (type == COMMAND) {
            handler = routes.get(RouteKey.of(null, COMMAND, value));
            if (handler != null) return handler;
        }

        // 4. Wildcard по value (CONTACT_INPUT + любой CONTACT/TEXT)
        handler = routes.get(RouteKey.of(state, type, null));
        if (handler != null) return handler;

        // 5. Wildcard по всему событию (IDLE + что угодно)
        handler = routes.get(RouteKey.of(state, null, null));
        if (handler != null) return handler;

        return fallbackHandler;
    }
}
