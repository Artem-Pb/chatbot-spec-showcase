package com.showcase.tablebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.showcase.tablebot.domain.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByActiveTrue();
}
