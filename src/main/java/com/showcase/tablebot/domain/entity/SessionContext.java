package com.showcase.tablebot.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранится как jsonb в БД. ignoreUnknown — чтобы поля, удалённые из класса в новой версии кода,
 * не роняли десериализацию уже сохранённых старых сессий.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionContext {

    private Integer slotPage;
    private LocalDate selectedDate;
    private String selectedDatetime;
    private String clientName;
    private String clientPhone;
    private List<String> slotOptions = new ArrayList<>();
}
