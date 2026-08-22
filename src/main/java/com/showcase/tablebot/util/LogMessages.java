package com.showcase.tablebot.util;

public final class LogMessages {

    private LogMessages() {}

    // --- TelegramWebhookController ---
    public static final String TG_INVALID_SECRET = "Telegram webhook: неверный секрет";
    public static final String TG_SEND_ERROR = "Ошибка отправки сообщения в Telegram: chatId={}, error={}";

    // --- TelegramWebhookRegistrar ---
    public static final String TG_WEBHOOK_OK = "Telegram webhook зарегистрирован: {}";
    public static final String TG_WEBHOOK_ERROR = "Ошибка регистрации Telegram webhook: {}";

    // --- UpdateProcessor ---
    public static final String UPDATE_DUPLICATE = "Дубликат апдейта проигнорирован: platform={}, externalId={}";
    public static final String UPDATE_OPTIMISTIC_LOCK = "Конфликт параллельных апдейтов (platform={}, user={}), пропускаем";
    public static final String UPDATE_INCOMING = ">>> [{}] user={} type={} value={}";
    public static final String UPDATE_TRANSITION = "    {} : {} → {}  (handler={})";
    public static final String UPDATE_DONE = "<<< [{}] user={} ответ отправлен";

    // --- StubNotificationService / TelegramNotificationService ---
    public static final String NOTIFICATION_ADMIN = "Уведомление админу: бронь #{}, клиент {}, столик {}, время {}";
}
