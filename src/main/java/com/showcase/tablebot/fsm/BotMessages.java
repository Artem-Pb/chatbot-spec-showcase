package com.showcase.tablebot.fsm;

public final class BotMessages {

    private BotMessages() {}

    // ===== ТЕКСТЫ СООБЩЕНИЙ =====

    // --- Общие ---
    public static final String FALLBACK = "Пожалуйста, выберите вариант из предложенных кнопок.";
    public static final String RESET_DONE = "Диалог сброшен. Напишите что-нибудь чтобы начать заново.";

    // --- Приветствие ---
    public static final String WELCOME =
            "Добро пожаловать в Wren & Table! Здесь можно почитать про наши столики, " +
            "задать вопрос и забронировать удобное время.\n" +
            "Выберите зону, которая вас интересует — я расскажу подробнее. 🙂";

    public static final String FAREWELL =
            "Спасибо, что заглянули! Будем рады видеть вас снова — нажмите «Начать», когда будете готовы.";

    // --- Меню зоны (после выбора столика) ---
    public static final String ZONE_MENU_INTRO = "Что вас интересует по этому столику?";

    // --- Инфо по столикам ---
    public static final String TABLE_1_DESCRIPTION =
            "Столик у окна — уютное место с видом на улицу, рассчитан на 2-4 гостя.\n" +
            "Хорошо подходит для завтрака или спокойного ужина вдвоём.";
    public static final String TABLE_2_DESCRIPTION =
            "VIP-кабинка — закрытое пространство для 6-8 гостей с отдельным входом.\n" +
            "Подойдёт для семейного праздника или деловой встречи, где важна приватность.";
    public static final String TABLE_3_DESCRIPTION =
            "Барная стойка — высокие места у бара, компания до 4 человек.\n" +
            "Живая атмосфера, рядом бармен и открытая кухня — хорошо для короткого визита компанией.";

    // --- Консультация ---
    public static final String CONSULTATION_INFO =
            "Если у вас есть вопрос о меню, аллергенах, парковке или особых пожеланиях к визиту — " +
            "напишите его в сообщении, и администратор ответит в ближайшее время.\n" +
            "Также можно позвонить нам напрямую по телефону, указанному в профиле.";

    // --- FAQ ---
    public static final String FAQ_TEXT =
            "Можно ли отменить бронь?\n" +
            "Да, свободно, если сообщить не позднее чем за 2 часа до времени брони.\n\n" +
            "Есть ли депозит за бронирование?\n" +
            "Для компаний до 6 человек — нет. Для больших групп и VIP-кабинки уточняем при подтверждении.\n\n" +
            "Можно ли прийти с животным?\n" +
            "Да, на летней террасе. В основном зале — только сервисные собаки.\n\n" +
            "Сколько по времени держится столик?\n" +
            "Стандартная бронь — на 90 минут, для VIP-кабинки — на 2 часа. Если нужно дольше, напишите об этом при бронировании.";

    // --- Запись ---
    public static final String SLOT_DATE_QUESTION = "Выберите дату визита:";
    public static final String SLOT_QUESTION = "Выберите удобное время:";
    public static final String SLOT_NO_SLOTS = "На выбранную дату свободных столиков нет. Попробуйте другой день:";
    public static final String CONTACT_QUESTION = "Отлично! Осталось поделиться контактом — и мы подтвердим бронь:";
    public static final String BOOKING_SUCCESS = "Столик забронирован! Ждём вас в указанное время.";
    public static final String BOOKING_CONFLICT = "К сожалению, выбранное время уже занято. Пожалуйста, выберите другое:";

    // ===== КНОПКИ =====

    // --- Главное меню (выбор зоны) ---
    public static final String BTN_TABLE_1 = "🪟 Столик у окна";
    public static final String BTN_TABLE_2 = "👑 VIP-кабинка";
    public static final String BTN_TABLE_3 = "🍸 Барная стойка";

    // --- Меню зоны ---
    public static final String BTN_ZONE_INFO = "📖 Подробнее";
    public static final String BTN_ZONE_ASK = "💬 Задать вопрос";
    public static final String BTN_ZONE_BOOK = "📅 Забронировать";
    public static final String BTN_ZONE_FAQ = "❓ Частые вопросы";

    // --- Навигация ---
    public static final String BTN_BACK = "Назад";
    public static final String BTN_START_OVER = "Начать";
    public static final String BTN_TO_MENU = "Вернуться в меню";
    public static final String BTN_WANT_BOOK = "Забронировать";

    // --- Запись ---
    public static final String BTN_SHARE_CONTACT = "Поделиться контактом";
    public static final String BTN_SLOT_PREV = "← Назад";
    public static final String BTN_SLOT_NEXT = "Ещё →";
}
