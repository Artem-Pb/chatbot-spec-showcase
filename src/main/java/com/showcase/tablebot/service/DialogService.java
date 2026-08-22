package com.showcase.tablebot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.showcase.tablebot.domain.entity.ChatUser;
import com.showcase.tablebot.domain.entity.SessionContext;
import com.showcase.tablebot.domain.entity.UserSession;
import com.showcase.tablebot.domain.enums.DialogState;
import com.showcase.tablebot.domain.enums.EventType;
import com.showcase.tablebot.dto.*;
import com.showcase.tablebot.fsm.DialogHandler;
import com.showcase.tablebot.fsm.DialogRouter;
import com.showcase.tablebot.repository.ChatUserRepository;
import com.showcase.tablebot.repository.UserSessionRepository;
import com.showcase.tablebot.util.LogMessages;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DialogService {

    private final ChatUserRepository chatUserRepository;
    private final UserSessionRepository userSessionRepository;
    private final DialogRouter dialogRouter;

    public ProcessOutcome process(IncomingMessage msg) {
        ChatUser user = findOrCreateUser(msg);
        UserSession session = findOrCreateSession(user);
        resetIfExpired(session);

        IncomingEvent event = buildEvent(msg);
        DialogState stateBefore = session.getState();
        DialogHandler handler = dialogRouter.resolve(stateBefore, event);
        HandlerResult result = handler.handle(new DialogContext(msg, session, user));

        session.setState(result.nextState());
        session.setUpdatedAt(LocalDateTime.now());
        userSessionRepository.save(session);

        log.info(LogMessages.UPDATE_TRANSITION,
                msg.platformUserId(), stateBefore, result.nextState(),
                handler.getClass().getSimpleName());

        return new ProcessOutcome(result.message(), result.booking());
    }

    private ChatUser findOrCreateUser(IncomingMessage msg) {
        return chatUserRepository
                .findByPlatformAndPlatformUserIdAndPlatformGroupId(
                        msg.platform(), msg.platformUserId(), msg.platformGroupId())
                .orElseGet(() -> {
                    ChatUser u = new ChatUser();
                    u.setPlatform(msg.platform());
                    u.setPlatformUserId(msg.platformUserId());
                    u.setPlatformGroupId(msg.platformGroupId());
                    u.setCreatedAt(LocalDateTime.now());
                    return chatUserRepository.save(u);
                });
    }

    private UserSession findOrCreateSession(ChatUser user) {
        return userSessionRepository
                .findByChatUserId(user.getId())
                .orElseGet(() -> {
                    UserSession ur = new UserSession();
                    ur.setChatUser(user);
                    ur.setState(DialogState.IDLE);
                    ur.setContext(new SessionContext());
                    ur.setUpdatedAt(LocalDateTime.now());
                    return userSessionRepository.save(ur);
                });
    }

    private void resetIfExpired(UserSession session) {
        DialogState state = session.getState();
        boolean isTerminal = state == DialogState.COMPLETED;
        boolean isExpired = session.getUpdatedAt()
                .isBefore(LocalDateTime.now().minusHours(24));

        if (isTerminal && isExpired) {
            session.setState(DialogState.IDLE);
            session.setContext(new SessionContext());
        }
    }

    private IncomingEvent buildEvent(IncomingMessage msg) {
        if (msg.callbackData() != null)
            return new IncomingEvent(EventType.CALLBACK, msg.callbackData());

        if (msg.text() != null && msg.text().startsWith("/"))
            return new IncomingEvent(EventType.COMMAND, msg.text());

        if (msg.contactPhone() != null)
            return new IncomingEvent(EventType.CONTACT, msg.contactPhone());

        return new IncomingEvent(EventType.TEXT, msg.text());
    }
}
