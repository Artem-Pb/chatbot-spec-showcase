package com.showcase.tablebot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_table")
@Getter @Setter @NoArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "capacity_seats", nullable = false)
    private Integer capacitySeats;

    @Column(name = "default_duration_minutes", nullable = false)
    private Integer defaultDurationMinutes;

    @Column(name = "booking_provider_table_id")
    private String bookingProviderTableId;

    @Column(nullable = false)
    private Boolean active = true;
}
