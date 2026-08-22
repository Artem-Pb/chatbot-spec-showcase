package com.showcase.tablebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.showcase.tablebot.domain.entity.ProcessedUpdate;

public interface ProcessedUpdateRepository extends JpaRepository<ProcessedUpdate, Long> {
}
