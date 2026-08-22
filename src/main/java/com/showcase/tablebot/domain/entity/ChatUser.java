package com.showcase.tablebot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.showcase.tablebot.domain.enums.Platform;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_user")
@Setter @Getter @NoArgsConstructor
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(name = "platform_user_id", nullable = false)
    private String platformUserId;

    @Column(name = "platform_group_id")
    private String platformGroupId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
