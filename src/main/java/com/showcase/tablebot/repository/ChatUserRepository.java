package com.showcase.tablebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.showcase.tablebot.domain.entity.ChatUser;
import com.showcase.tablebot.domain.enums.Platform;

import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    Optional<ChatUser> findByPlatformAndPlatformUserIdAndPlatformGroupId(Platform platform,
                                                                         String platformUserId,
                                                                         String platformGroupId);
}
