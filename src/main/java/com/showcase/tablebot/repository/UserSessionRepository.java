package com.showcase.tablebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.showcase.tablebot.domain.entity.UserSession;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByChatUserId(Long chatUserId);
}
