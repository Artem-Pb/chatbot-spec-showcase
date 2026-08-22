package com.showcase.tablebot.service;

import com.showcase.tablebot.domain.entity.TableBooking;

public interface NotificationService {
    void notifyAdmin(TableBooking booking);
}
