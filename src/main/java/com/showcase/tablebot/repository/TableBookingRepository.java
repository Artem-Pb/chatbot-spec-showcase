package com.showcase.tablebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.showcase.tablebot.domain.entity.TableBooking;

public interface TableBookingRepository extends JpaRepository<TableBooking, Long> {
}
