-- =====================================================================
-- GameStore - data.sql (datos de ejemplo). Ids explícitos para enlazar las claves foráneas.
-- =====================================================================

-- ---------- Categoria ----------
INSERT INTO categoria (id, nombre, descripcion, activa) VALUES
(1, 'Acción',      'Juegos de ritmo rápido, combates y reflejos.', TRUE),
(2, 'Aventura',    'Exploración, puzles e historias inmersivas.', TRUE),
(3, 'RPG',         'Juegos de rol con progresión de personaje y mundos abiertos.', TRUE),
(4, 'Deportes',    'Simuladores de fútbol, baloncesto, carreras y más.', TRUE),
(5, 'Estrategia',  'Planificación, gestión de recursos y toma de decisiones.', TRUE),
(6, 'Indie',       'Títulos independientes con propuestas originales.', TRUE),
(7, 'Simulación',  'Simuladores de vida, vuelo y gestión (categoría en desuso).', FALSE);

-- ---------- FichaTecnica ----------
INSERT INTO ficha_tecnica (id, sistema_operativo, procesador, memoria_ram_gb, espacio_disco_gb, requiere_internet) VALUES
(1,  'Nintendo Switch OS', 'NVIDIA Custom Tegra X1',  4,  16, FALSE),
(2,  'PlayStation 5 OS',   'AMD Zen 2 8 núcleos',    16,  50, FALSE),
(3,  'PlayStation 5 OS',   'AMD Zen 2 8 núcleos',    16, 100, TRUE),
(4,  'Nintendo Switch OS', 'NVIDIA Custom Tegra X1',  4,   3, FALSE),
(5,  'Windows 10 64-bit',  'Intel Core i5-4430',      8,  15, FALSE),
(6,  'Windows 10 64-bit',  'Intel Core i7-6700',     12,  70, FALSE),
(7,  'Nintendo Switch OS', 'NVIDIA Custom Tegra X1',  4,   8, TRUE),
(8,  'PlayStation 5 OS',   'AMD Zen 2 8 núcleos',    16,  90, FALSE),
(9,  'Windows 11 64-bit',  'AMD Ryzen 5 3600',       16,  60, FALSE),
(10, 'Windows 10 64-bit',  'Intel Core i3-3240',      4,   5, FALSE);

-- ---------- Plataforma ----------
INSERT INTO plataforma (id, nombre, fabricante, generacion, fecha_lanzamiento, es_portatil) VALUES
(1, 'Nintendo Switch', 'Nintendo',  'Octava',    '2017-03-03', TRUE),
(2, 'PlayStation 5',   'Sony',      'Novena',    '2020-11-12', FALSE),
(3, 'Xbox Series X',   'Microsoft', 'Novena',    '2020-11-10', FALSE),
(4, 'PC (Windows)',    'Microsoft', 'No aplica', '1985-11-20', FALSE),
(5, 'Steam Deck',      'Valve',     'Octava',    '2022-02-25', TRUE);

-- ---------- Desarrolladora ----------
INSERT INTO desarrolladora (id, nombre, pais_origen, fecha_fundacion, pagina_web) VALUES
(1, 'Nintendo',            'Japón',          '1889-09-23', 'https://www.nintendo.com'),
(2, 'FromSoftware',        'Japón',          '1986-11-01', 'https://www.fromsoftware.jp'),
(3, 'Electronic Arts',     'Estados Unidos', '1982-05-27', 'https://www.ea.com'),
(4, 'Supergiant Games',    'Estados Unidos', '2009-07-01', 'https://www.supergiantgames.com'),
(5, 'Firaxis Games',       'Estados Unidos', '1996-05-01', 'https://www.firaxis.com'),
(6, 'CD Projekt Red',      'Polonia',        '2002-02-01', 'https://www.cdprojektred.com'),
(7, 'Santa Monica Studio', 'Estados Unidos', '1999-01-01', 'https://sms.playstation.com'),
(8, 'ConcernedApe',        'Estados Unidos', '2012-01-01', 'https://www.stardewvalley.net');

-- ---------- Videojuego ----------
-- (id, titulo, descripcion, precio, stock, fecha_lanzamiento, imagen, es_multijugador,
--  categoria_id, ficha_tecnica_id, plataforma_id, desarrolladora_id)
INSERT INTO videojuego (id, titulo, descripcion, precio, stock, fecha_lanzamiento, imagen, es_multijugador, categoria_id, ficha_tecnica_id, plataforma_id, desarrolladora_id) VALUES
(1,  'The Legend of Zelda: Tears of the Kingdom', 'Link explora los cielos y las profundidades de Hyrule en esta aventura épica.', 59.99, 25, '2023-05-12', NULL, FALSE, 2, 1,    1, 1),
(2,  'Elden Ring',                                'Un enorme mundo abierto de fantasía oscura creado junto a George R. R. Martin.', 59.99, 18, '2022-02-25', NULL, FALSE, 3, 2,    2, 2),
(3,  'EA Sports FC 24',                           'La nueva generación del fútbol con HyperMotionV y modos online.', 69.99, 40, '2023-09-29', NULL, TRUE,  4, 3,    2, 3),
(4,  'Hades',                                     'Roguelike de acción en el que escapas del inframundo griego.', 24.99, 30, '2020-09-17', NULL, FALSE, 6, 4,    1, 4),
(5,  'Sid Meier''s Civilization VI',              'Construye un imperio capaz de resistir el paso del tiempo.', 29.99, 15, '2016-10-21', NULL, TRUE,  5, 5,    4, 5),
(6,  'Cyberpunk 2077',                            'RPG de acción en Night City, una megalópolis obsesionada con el poder.', 39.99, 22, '2020-12-10', NULL, FALSE, 3, 6,    4, 6),
(7,  'Mario Kart 8 Deluxe',                       'Carreras frenéticas con objetos y circuitos de toda la saga.', 49.99, 35, '2017-04-28', NULL, TRUE,  1, 7,    1, 1),
(8,  'God of War Ragnarök',                       'Kratos y Atreus se enfrentan al fin del mundo nórdico.', 69.99, 20, '2022-11-09', NULL, FALSE, 1, 8,    2, 7),
(9,  'Stardew Valley',                            'Hereda una granja, cultiva, pesca y haz amigos en el pueblo.', 14.99, 50, '2016-02-26', NULL, TRUE,  6, NULL, 4, 8),
(10, 'The Witcher 3: Wild Hunt',                  'Geralt de Rivia busca a su hija adoptiva en un vasto mundo abierto.', 29.99, 12, '2015-05-19', NULL, FALSE, 3, NULL, 4, 6);

-- ---------- Clientes ----------
INSERT INTO clientes (id, nombre, apellidos, dni, correo, telefono, fecha_registro, direccion, es_premium) VALUES
(1, 'Lucía',  'Martín Gómez',   '12345678Z', 'lucia.martin@correo.es',   '600111222', '2023-01-15', 'Calle Sol 12, Sevilla',     TRUE),
(2, 'Pablo',  'Ruiz Fernández', '23456789D', 'pablo.ruiz@correo.es',     '600222333', '2023-03-02', 'Av. de la Paz 45, Carmona', FALSE),
(3, 'Marta',  'López Sánchez',  '34567890V', 'marta.lopez@correo.es',    '600333444', '2023-06-21', 'Calle Mayor 7, Córdoba',    FALSE),
(4, 'Andrés', 'Navarro Ortega', '45678901G', 'andres.navarro@correo.es', '600444555', '2024-02-10', 'Plaza España 3, Málaga',    TRUE),
(5, 'Elena',  'Díaz Romero',    '56789012B', 'elena.diaz@correo.es',     '600555666', '2024-09-30', NULL,                        FALSE);

-- ---------- Pedidos ----------
-- (id, fecha, precio_total, estado, direccion_envio, numero_seguimiento, cliente_id)
INSERT INTO pedidos (id, fecha, precio_total, estado, direccion_envio, numero_seguimiento, cliente_id) VALUES
(1, '2024-03-05',  84.98, 'ENTREGADO', 'Calle Sol 12, Sevilla',     'ES-TRK-000111', 1),
(2, '2024-04-12',  59.99, 'ENTREGADO', 'Av. de la Paz 45, Carmona', 'ES-TRK-000222', 2),
(3, '2024-05-20', 119.98, 'ENVIADO',   'Calle Mayor 7, Córdoba',    'ES-TRK-000333', 3),
(4, '2024-06-01',  69.98, 'PENDIENTE', 'Plaza España 3, Málaga',    NULL,            4),
(5, '2024-06-02',  29.99, 'CANCELADO', 'Calle Luna 22, Granada',    NULL,            5);

-- ---------- Pedido_Videojuego (ManyToMany) ----------
INSERT INTO pedido_videojuego (pedido_id, videojuego_id) VALUES
(1, 1), (1, 4),
(2, 2),
(3, 3), (3, 7),
(4, 6), (4, 10),
(5, 5);

-- ---------- Facturas (una por pedido; sin factura los pedidos pendiente y cancelado) ----------
INSERT INTO facturas (id, numero_factura, fecha_emision, importe_total, pagada, metodo_pago, pedido_id) VALUES
(1, 'F-2024-0001', '2024-03-05',  84.98, TRUE,  'Tarjeta',       1),
(2, 'F-2024-0002', '2024-04-12',  59.99, TRUE,  'PayPal',        2),
(3, 'F-2024-0003', '2024-05-20', 119.98, FALSE, NULL,            3);
