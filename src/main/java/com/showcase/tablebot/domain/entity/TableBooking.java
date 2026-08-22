package com.showcase.tablebot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.showcase.tablebot.domain.enums.BookingStatus;
import com.showcase.tablebot.domain.enums.Platform;

import java.time.LocalDateTime;

@Entity
@Table(name = "table_booking")
@Getter @Setter @NoArgsConstructor
public class TableBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_id", nullable = false)
    private ChatUser chatUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(name = "platform_group_id")
    private String platformGroupId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_phone")
    private String clientPhone;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "booking_provider_reservation_id")
    private String bookingProviderReservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
