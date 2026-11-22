-- =====================================================================
-- GameStore - data.sql (datos de ejemplo de todas las tablas)
-- Los ids se asignan en orden de inserción (1, 2, 3...), por eso las claves
-- foráneas pueden referirse a ellos directamente.
-- =====================================================================

-- ---------- Categoria ----------
INSERT INTO categoria (nombre, descripcion, activa) VALUES
('Acción',      'Juegos de ritmo rápido, combates y reflejos.', TRUE),
('Aventura',    'Exploración, puzles e historias inmersivas.', TRUE),
('RPG',         'Juegos de rol con progresión de personaje y mundos abiertos.', TRUE),
('Deportes',    'Simuladores de fútbol, baloncesto, carreras y más.', TRUE),
('Estrategia',  'Planificación, gestión de recursos y toma de decisiones.', TRUE),
('Indie',       'Títulos independientes con propuestas originales.', TRUE),
('Simulación',  'Simuladores de vida, vuelo y gestión (categoría en desuso).', FALSE);

-- ---------- FichaTecnica ----------
INSERT INTO ficha_tecnica (sistema_operativo, procesador, memoria_ram_gb, espacio_disco_gb, requiere_internet) VALUES
('Nintendo Switch OS', 'NVIDIA Custom Tegra X1',        4,  16, FALSE),
('PlayStation 5 OS',   'AMD Zen 2 8 núcleos',          16, 50,  FALSE),
('PlayStation 5 OS',   'AMD Zen 2 8 núcleos',          16, 100, TRUE),
('Nintendo Switch OS', 'NVIDIA Custom Tegra X1',        4,  3,  FALSE),
('Windows 10 64-bit',  'Intel Core i5-4430',            8,  15, FALSE),
('Windows 10 64-bit',  'Intel Core i7-6700',           12, 70,  FALSE),
('Nintendo Switch OS', 'NVIDIA Custom Tegra X1',        4,  8,  TRUE),
('PlayStation 5 OS',   'AMD Zen 2 8 núcleos',          16, 90,  FALSE),
('Windows 11 64-bit',  'AMD Ryzen 5 3600',             16, 60,  FALSE),
('Windows 10 64-bit',  'Intel Core i3-3240',            4,  5,  FALSE);

-- ---------- Plataforma ----------
INSERT INTO plataforma (nombre, fabricante, generacion, fecha_lanzamiento, es_portatil) VALUES
('Nintendo Switch', 'Nintendo',  8, '2017-03-03', TRUE),
('PlayStation 5',   'Sony',      9, '2020-11-12', FALSE),
('Xbox Series X',   'Microsoft', 9, '2020-11-10', FALSE),
('PC (Windows)',    'Microsoft', 1, '1985-11-20', FALSE),
('Steam Deck',      'Valve',     1, '2022-02-25', TRUE);

-- ---------- Desarrolladora ----------
INSERT INTO desarrolladora (nombre, pais, fecha_fundacion, pagina_web) VALUES
('Nintendo',           'Japón',          '1889-09-23', 'https://www.nintendo.com'),
('FromSoftware',       'Japón',          '1986-11-01', 'https://www.fromsoftware.jp'),
('Electronic Arts',    'Estados Unidos', '1982-05-27', 'https://www.ea.com'),
('Supergiant Games',   'Estados Unidos', '2009-07-01', 'https://www.supergiantgames.com'),
('Firaxis Games',      'Estados Unidos', '1996-05-01', 'https://www.firaxis.com'),
('CD Projekt Red',     'Polonia',        '2002-02-01', 'https://www.cdprojektred.com'),
('Santa Monica Studio','Estados Unidos', '1999-01-01', 'https://sms.playstation.com'),
('ConcernedApe',       'Estados Unidos', '2012-01-01', 'https://www.stardewvalley.net');

-- ---------- Videojuego ----------
-- (titulo, descripcion, precio, stock, fecha_lanzamiento, imagen, es_multijugador,
--  categoria_id, ficha_tecnica_id, plataforma_id, desarrolladora_id)
INSERT INTO videojuego (titulo, descripcion, precio, stock, fecha_lanzamiento, imagen, es_multijugador, categoria_id, ficha_tecnica_id, plataforma_id, desarrolladora_id) VALUES
('The Legend of Zelda: Tears of the Kingdom', 'Link explora los cielos y las profundidades de Hyrule en esta aventura épica.', 59.99, 25, '2023-05-12', NULL, FALSE, 2, 1, 1, 1),
('Elden Ring',                                'Un enorme mundo abierto de fantasía oscura creado junto a George R. R. Martin.', 59.99, 18, '2022-02-25', NULL, FALSE, 3, 2, 2, 2),
('EA Sports FC 24',                           'La nueva generación del fútbol con HyperMotionV y modos online.', 69.99, 40, '2023-09-29', NULL, TRUE,  4, 3, 2, 3),
('Hades',                                     'Roguelike de acción en el que escapas del inframundo griego.', 24.99, 30, '2020-09-17', NULL, FALSE, 6, 4, 1, 4),
('Sid Meier''s Civilization VI',              'Construye un imperio capaz de resistir el paso del tiempo.', 29.99, 15, '2016-10-21', NULL, TRUE,  5, 5, 4, 5),
('Cyberpunk 2077',                            'RPG de acción en Night City, una megalópolis obsesionada con el poder.', 39.99, 22, '2020-12-10', NULL, FALSE, 3, 6, 4, 6),
('Mario Kart 8 Deluxe',                       'Carreras frenéticas con objetos y circuitos de toda la saga.', 49.99, 35, '2017-04-28', NULL, TRUE,  1, 7, 1, 1),
('God of War Ragnarök',                       'Kratos y Atreus se enfrentan al fin del mundo nórdico.', 69.99, 20, '2022-11-09', NULL, FALSE, 1, 8, 2, 7),
('Stardew Valley',                            'Hereda una granja, cultiva, pesca y haz amigos en el pueblo.', 14.99, 50, '2016-02-26', NULL, TRUE,  6, NULL, 4, 8),
('The Witcher 3: Wild Hunt',                  'Geralt de Rivia busca a su hija adoptiva en un vasto mundo abierto.', 29.99, 12, '2015-05-19', NULL, FALSE, 3, NULL, 4, 6);

-- ---------- Cliente ----------
INSERT INTO cliente (nombre, apellidos, dni, correo, telefono, fecha_registro, direccion, es_premium) VALUES
('Lucía',  'Martín Gómez',     '12345678A', 'lucia.martin@correo.es',  '600111222', '2023-01-15', 'Calle Sol 12, Sevilla',        TRUE),
('Pablo',  'Ruiz Fernández',   '23456789B', 'pablo.ruiz@correo.es',    '600222333', '2023-03-02', 'Av. de la Paz 45, Carmona',    FALSE),
('Marta',  'López Sánchez',    '34567890C', 'marta.lopez@correo.es',   '600333444', '2023-06-21', 'Calle Mayor 7, Córdoba',       FALSE),
('Andrés', 'Navarro Ortega',   '45678901D', 'andres.navarro@correo.es','600444555', '2024-02-10', 'Plaza España 3, Málaga',       TRUE),
('Elena',  'Díaz Romero',      '56789012E', 'elena.diaz@correo.es',    '600555666', '2024-09-30', 'Calle Luna 22, Granada',       FALSE);

-- ---------- Factura ----------
INSERT INTO factura (numero, fecha_emision, importe) VALUES
('F-2024-0001', '2024-03-05', 84.98),
('F-2024-0002', '2024-04-12', 59.99),
('F-2024-0003', '2024-05-20', 119.98);

-- ---------- Pedido ----------
-- (fecha, precio_total, estado, direccion_envio, numero_seguimiento, cliente_id, factura_id)
INSERT INTO pedido (fecha, precio_total, estado, direccion_envio, numero_seguimiento, cliente_id, factura_id) VALUES
('2024-03-05 10:30:00',  84.98, 'ENTREGADO', 'Calle Sol 12, Sevilla',     'ES-TRK-000111', 1, 1),
('2024-04-12 18:05:00',  59.99, 'ENTREGADO', 'Av. de la Paz 45, Carmona', 'ES-TRK-000222', 2, 2),
('2024-05-20 12:45:00', 119.98, 'ENVIADO',   'Calle Mayor 7, Córdoba',    'ES-TRK-000333', 3, 3),
('2024-06-01 09:15:00',  69.98, 'PENDIENTE', 'Plaza España 3, Málaga',    NULL,            4, NULL),
('2024-06-02 20:00:00',  29.99, 'CANCELADO', 'Calle Luna 22, Granada',    NULL,            5, NULL);

-- ---------- Pedido_Videojuego (ManyToMany) ----------
INSERT INTO pedido_videojuego (pedido_id, videojuego_id) VALUES
(1, 1), (1, 4),
(2, 2),
(3, 3), (3, 7),
(4, 6), (4, 10),
(5, 5);
