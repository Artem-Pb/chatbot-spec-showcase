package com.showcase.tablebot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.showcase.tablebot.domain.enums.DialogState;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_session")
@Getter @Setter @NoArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_id", nullable = false, unique = true)
    private ChatUser chatUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DialogState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_table_id")
    private RestaurantTable selectedTable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context", nullable = false, columnDefinition = "jsonb")
    private SessionContext context = new SessionContext();

    @Version
    private Long version;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
