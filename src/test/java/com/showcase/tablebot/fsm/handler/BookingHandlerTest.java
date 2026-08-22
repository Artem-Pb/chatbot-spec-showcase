package com.showcase.tablebot.fsm.handler;

import com.showcase.tablebot.domain.entity.ChatUser;
import com.showcase.tablebot.domain.entity.RestaurantTable;
import com.showcase.tablebot.domain.entity.SessionContext;
import com.showcase.tablebot.domain.entity.UserSession;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.dto.*;
import com.showcase.tablebot.repository.TableBookingRepository;
import com.showcase.tablebot.service.BookingProviderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * BookingHandler — самый содержательный хендлер флоу: делает внешний вызов
 * провайдера бронирования, обрабатывает конфликт слота (провайдер мог отдать
 * устаревший список слотов) и только при успехе сохраняет TableBooking.
 * Оба пути (успех / гонка за слот) покрыты до перехода к следующей задаче.
 */
class BookingHandlerTest {

    private BookingProviderClient bookingProviderClient;
    private TableBookingRepository tableBookingRepository;
    private BookingHandler handler;

    private ChatUser user;
    private UserSession session;
    private RestaurantTable table;

    @BeforeEach
    void setUp() {
        bookingProviderClient = mock(BookingProviderClient.class);
        tableBookingRepository = mock(TableBookingRepository.class);
        handler = new BookingHandler(bookingProviderClient, tableBookingRepository);

        user = new ChatUser();
        user.setPlatform(Platform.TELEGRAM);
        user.setPlatformUserId("100");

        table = new RestaurantTable();
        table.setId(1L);
        table.setName("Столик у окна");
        table.setBookingProviderTableId("table-window");

        SessionContext context = new SessionContext();
        context.setSelectedDatetime(LocalDateTime.of(2026, 9, 1, 19, 0).toString());
        context.setSelectedDate(LocalDate.of(2026, 9, 1));

        session = new UserSession();
        session.setState(DialogState.CONTACT_INPUT);
        session.setChatUser(user);
        session.setSelectedTable(table);
        session.setContext(context);
    }

    @Test
    void slotAvailable_savesBooking_andMovesToAwaitingConfirmation() {
        when(bookingProviderClient.book(any()))
                .thenReturn(new BookingProviderResult(true, "res-123", false, null));

        IncomingMessage msg = new IncomingMessage(
                Platform.TELEGRAM, "100", "100", "ext-1", null,
                null, null, "+79991234567", "Иван", "{}");
        DialogContext ctx = new DialogContext(msg, session, user);

        HandlerResult result = handler.handle(ctx);

        assertThat(result.nextState()).isEqualTo(DialogState.AWAITING_CONFIRMATION);
        assertThat(result.booking()).isPresent();
        assertThat(result.booking().get().getClientPhone()).isEqualTo("+79991234567");
        verify(tableBookingRepository, times(1)).save(any());
    }

    @Test
    void slotTaken_doesNotSaveBooking_offersFreshSlots() {
        when(bookingProviderClient.book(any()))
                .thenReturn(new BookingProviderResult(false, null, true, "slot taken"));
        when(bookingProviderClient.getAvailableSlots(eq("table-window"), any()))
                .thenReturn(List.of(
                        new BookingProviderSlot("19:30", LocalDateTime.of(2026, 9, 1, 19, 30), 5400)
                ));

        IncomingMessage msg = new IncomingMessage(
                Platform.TELEGRAM, "100", "100", "ext-2", null,
                null, null, "+79991234567", "Иван", "{}");
        DialogContext ctx = new DialogContext(msg, session, user);

        HandlerResult result = handler.handle(ctx);

        assertThat(result.nextState()).isEqualTo(DialogState.SLOT_SELECTION);
        assertThat(result.booking()).isEmpty();
        verify(tableBookingRepository, never()).save(any());
    }
}
