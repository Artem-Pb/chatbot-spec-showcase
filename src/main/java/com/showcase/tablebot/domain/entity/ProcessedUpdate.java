package com.showcase.tablebot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.showcase.tablebot.domain.enums.Platform;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_update",
        uniqueConstraints = @UniqueConstraint(columnNames = {"platform", "external_id"}))
@Getter @Setter @NoArgsConstructor
public class ProcessedUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "processed_at", nullable = false, updatable = false)
    private LocalDateTime processedAt;
}
