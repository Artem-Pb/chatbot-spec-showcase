INSERT INTO restaurant_table (id, name, description, capacity_seats, default_duration_minutes, booking_provider_table_id, active)
SELECT 1, 'Столик у окна', 'Уютное место с видом на улицу, рассчитан на 2-4 гостя.', 4, 90, 'table-window', true
WHERE NOT EXISTS (SELECT 1 FROM restaurant_table WHERE id = 1);

INSERT INTO restaurant_table (id, name, description, capacity_seats, default_duration_minutes, booking_provider_table_id, active)
SELECT 2, 'VIP-кабинка', 'Закрытое пространство для 6-8 гостей с отдельным входом.', 8, 120, 'table-vip', true
WHERE NOT EXISTS (SELECT 1 FROM restaurant_table WHERE id = 2);

INSERT INTO restaurant_table (id, name, description, capacity_seats, default_duration_minutes, booking_provider_table_id, active)
SELECT 3, 'Барная стойка', 'Высокие места у бара, компания до 4 человек.', 4, 90, 'table-bar', true
WHERE NOT EXISTS (SELECT 1 FROM restaurant_table WHERE id = 3);
