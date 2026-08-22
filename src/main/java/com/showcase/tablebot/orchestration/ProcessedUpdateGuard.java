package com.showcase.tablebot.orchestration;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import com.showcase.tablebot.domain.entity.ProcessedUpdate;
import com.showcase.tablebot.domain.enums.Platform;
import com.showcase.tablebot.repository.ProcessedUpdateRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProcessedUpdateGuard {

    private final ProcessedUpdateRepository processedUpdateRepository;
    private final PlatformTransactionManager transactionManager;

    public boolean isDuplicate(Platform platform, String externalId) {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        try {
            tx.executeWithoutResult(status -> {
                ProcessedUpdate record = new ProcessedUpdate();
                record.setPlatform(platform);
                record.setExternalId(externalId);
                record.setProcessedAt(LocalDateTime.now());
                processedUpdateRepository.saveAndFlush(record);
            });
            return false;
        } catch (DataIntegrityViolationException e) {
            return true;
        }
    }
}
