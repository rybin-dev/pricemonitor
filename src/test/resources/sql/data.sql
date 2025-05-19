INSERT INTO category (id, name)
VALUES (1, 'Фрукты'),
       (2, 'Овощи'),
       (3, 'Молочные продукты'),
       (4, 'Мясо'),
       (5, 'Хлебобулочные изделия'),
       (6, 'Бакалея'),
       (7, 'Замороженные продукты'),
       (8, 'Напитки');

SELECT SETVAL('category_id_seq', (SELECT MAX(id) FROM category));

INSERT INTO shop (id, name, info)
VALUES (1, 'Магнит', 'Линия 1'),
       (2, 'Пятёрочка', 'Линия 2'),
       (3, 'Ашан', 'Линия 3'),
       (4, 'Лента', 'Линия 4'),
       (5, 'Перекрёсток', 'Линия 5');

SELECT SETVAL('shop_id_seq', (SELECT MAX(id) FROM shop));

INSERT INTO product (id, name, info)
VALUES (1, 'Яблоко', 'info'),
       (2, 'Банан', 'info'),
       (3, 'Молоко 2.5%', 'info'),
       (4, 'Сыр Гауда', 'info'),
       (5, 'Хлеб ржаной', 'info'),
       (6, 'Хлеб белый', 'info'),
       (7, 'Курица филе', 'info'),
       (8, 'Свинина шея', 'info'),
       (9, 'Морковь', 'info'),
       (10, 'Картофель', 'info'),
       (11, 'Макароны', 'info'),
       (12, 'Гречка', 'info'),
       (13, 'Пельмени', 'info'),
       (14, 'Мороженое', 'info'),
       (15, 'Минеральная вода', 'info'),
       (16, 'Сок апельсиновый', 'info');

SELECT SETVAL('product_id_seq', (SELECT MAX(id) FROM product));

INSERT INTO product_category (product_id, category_id)
VALUES (1, 1),  -- Яблоко -> Фрукты
       (2, 1),  -- Банан -> Фрукты
       (3, 3),  -- Молоко -> Молочные
       (4, 3),  -- Сыр -> Молочные
       (5, 5),  -- Хлеб ржаной -> Хлеб
       (6, 5),  -- Хлеб белый -> Хлеб
       (7, 4),  -- Курица -> Мясо
       (8, 4),  -- Свинина -> Мясо
       (9, 2),  -- Морковь -> Овощи
       (10, 2), -- Картофель -> Овощи
       (11, 6), -- Макароны -> Бакалея
       (12, 6), -- Гречка -> Бакалея
       (13, 7), -- Пельмени -> Замороженные
       (14, 7), -- Мороженое -> Замороженные
       (15, 8), -- Минеральная вода -> Напитки
       (16, 8); -- Сок апельсиновый -> Напитки


INSERT INTO product_price (product_id, shop_id, price, start_date, end_date)
VALUES (1, 1, 90, '2025-04-01', '2025-04-16'),
       (1, 1, 85, '2025-04-16', NULL),
       (1, 2, 92, '2025-04-05', NULL),
       (2, 3, 100, '2025-04-01', '2025-04-21'),
       (2, 3, 95, '2025-04-21', NULL),

-- Молоко в 3 магазинах
       (3, 1, 70, '2025-04-01', '2025-04-11'),
       (3, 1, 68, '2025-04-11', NULL),
       (3, 4, 69, '2025-04-01', NULL),
       (3, 2, 71, '2025-04-03', NULL),

-- Хлеб белый
       (6, 1, 40, '2025-04-01', '2025-04-21'),
       (6, 1, 42, '2025-04-21', NULL),
       (6, 2, 39, '2025-04-10', NULL),

-- Пельмени
       (13, 4, 210, '2025-04-01', NULL),
       (13, 5, 220, '2025-04-07', NULL),

-- Минеральная вода
       (15, 2, 30, '2025-04-01', '2025-04-16'),
       (15, 2, 32, '2025-04-16', NULL),
       (15, 3, 29, '2025-04-05', NULL),

-- Свинина
       (8, 1, 340, '2025-04-01', '2025-04-21'),
       (8, 1, 355, '2025-04-21', NULL),
       (8, 4, 360, '2025-04-01', NULL),

-- Морковь
       (9, 1, 28, '2025-04-01', NULL),
       (9, 2, 30, '2025-04-05', NULL),

-- Сок
       (16, 2, 75, '2025-04-01', NULL),
       (16, 5, 80, '2025-04-10', NULL);

SELECT SETVAL('product_price_id_seq', (SELECT MAX(id) FROM product_price));

INSERT INTO users (id, username, password, role)
VALUES (1, 'user', '$2a$10$X2YgVO.UmZbt1hc9aLWdsOeHE59VUAsbQRT1Fg61cIJvyyF.CEVJ2', 'USER'),
       (2, 'admin', '$2a$10$X2YgVO.UmZbt1hc9aLWdsOeHE59VUAsbQRT1Fg61cIJvyyF.CEVJ2', 'ADMIN');

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO user_profile (user_id, name, avatar_url)
VALUES (1, 'sdfsdf', 'sdfsdfsdfsdf'),
       (2, 'sdfssdfdf', 'sdfsdfsdfsdfsdf')