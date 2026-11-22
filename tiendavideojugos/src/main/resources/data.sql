-- =====================================================================
-- data.sql - Datos de ejemplo (ids explícitos para poder enlazar FKs)
-- =====================================================================

-- DESARROLLADORAS
INSERT INTO desarrolladora (id, nombre, pais_origen, fecha_fundacion, pagina_web) VALUES
                                                                                      (1, 'Nintendo EPD',   'Japón',          '1889-09-23', 'https://www.nintendo.com'),
                                                                                      (2, 'Rockstar Games', 'Estados Unidos', '1998-12-01', 'https://www.rockstargames.com'),
                                                                                      (3, 'FromSoftware',   'Japón',          '1986-11-01', 'https://www.fromsoftware.jp'),
                                                                                      (4, 'Ubisoft',        'Francia',        '1986-03-28', 'https://www.ubisoft.com'),
                                                                                      (5, 'Mojang Studios', 'Suecia',         '2009-05-17', 'https://www.minecraft.net'),
                                                                                      (6, 'Naughty Dog',    'Estados Unidos', '1984-09-27', 'https://www.naughtydog.com');

-- PLATAFORMAS
INSERT INTO plataforma (id, nombre, fabricante, generacion, fecha_lanzamiento, es_portatil) VALUES
                                                                                                (1, 'PlayStation 5',    'Sony',      'Novena',  '2020-11-12', FALSE),
                                                                                                (2, 'Xbox Series X',    'Microsoft', 'Novena',  '2020-11-10', FALSE),
                                                                                                (3, 'Nintendo Switch',  'Nintendo',  'Octava',  '2017-03-03', TRUE),
                                                                                                (4, 'PlayStation 4',    'Sony',      'Octava',  '2013-11-15', FALSE),
                                                                                                (5, 'Steam Deck',       'Valve',     'Octava',  '2022-02-25', TRUE);

-- VIDEOJUEGOS (ajustar si tu entidad Videojuego es distinta)
INSERT INTO videojuego (id, titulo, genero, fecha_lanzamiento, precio, stock, desarrolladora_id, plataforma_id) VALUES
                                                                                                                    (1, 'Elden Ring',                              'RPG de acción',     '2022-02-25', 59.99, 40, 3, 1),
                                                                                                                    (2, 'Red Dead Redemption 2',                   'Aventura',          '2018-10-26', 49.99, 25, 2, 4),
                                                                                                                    (3, 'The Legend of Zelda: Tears of the Kingdom','Aventura',         '2023-05-12', 69.99, 30, 1, 3),
                                                                                                                    (4, 'Assassin''s Creed Mirage',                'Acción-aventura',   '2023-10-05', 39.99, 20, 4, 2),
                                                                                                                    (5, 'The Last of Us Part II',                  'Acción-aventura',   '2020-06-19', 29.99, 15, 6, 4),
                                                                                                                    (6, 'Minecraft',                               'Sandbox',           '2011-11-18', 26.95, 50, 5, 3),
                                                                                                                    (7, 'Super Mario Odyssey',                     'Plataformas',       '2017-10-27', 44.99, 18, 1, 3);

-- CLIENTES
INSERT INTO clientes (id, nombre, apellidos, dni, correo, telefono, fecha_registro, direccion, es_premium) VALUES
                                                                                                               (1, 'Lucía',  'Fernández Ruiz',   '12345678Z', 'lucia.fernandez@example.com', '+34600111222', '2024-01-15', 'Calle Larios 12, Málaga',          TRUE),
                                                                                                               (2, 'Carlos', 'Gómez Pérez',      '23456789D', 'carlos.gomez@example.com',    '600222333',    '2024-03-02', 'Avenida de Andalucía 45, Sevilla', FALSE),
                                                                                                               (3, 'Marta',  'López Sánchez',    '34567890V', 'marta.lopez@example.com',     '+34611333444', '2024-06-20', 'Calle Mayor 8, Fuengirola',        FALSE),
                                                                                                               (4, 'Javier', 'Martín Torres',    '45678901G', 'javier.martin@example.com',   '622444555',    '2025-02-11', NULL,                               TRUE),
                                                                                                               (5, 'Elena',  'Ortega Navarro',   '56789012B', 'elena.ortega@example.com',    '+34633555666', '2025-09-30', 'Plaza de España 3, Granada',       FALSE);

-- PEDIDOS
INSERT INTO pedidos (id, fecha, precio_total, estado, direccion_envio, numero_seguimiento, cliente_id) VALUES
                                                                                                           (1, '2026-09-05',  89.98, 'ENTREGADO',      'Calle Larios 12, Málaga',          'ES2026090501', 1),
                                                                                                           (2, '2026-10-12',  69.99, 'ENVIADO',        'Avenida de Andalucía 45, Sevilla', 'ES2026101202', 2),
                                                                                                           (3, '2026-11-03',  89.98, 'EN_PREPARACION', 'Calle Mayor 8, Fuengirola',        NULL,           3),
                                                                                                           (4, '2026-10-20',  71.94, 'ENTREGADO',      'Calle del Sol 21, Córdoba',        'ES2026102004', 4),
                                                                                                           (5, '2026-11-18',  59.99, 'PENDIENTE',      'Plaza de España 3, Granada',       NULL,           5),
                                                                                                           (6, '2026-08-14', 114.98, 'CANCELADO',      'Calle Larios 12, Málaga',          NULL,           1);

-- PEDIDO_VIDEOJUEGO
INSERT INTO pedido_videojuego (pedido_id, videojuego_id) VALUES
                                                             (1, 1), (1, 5),
                                                             (2, 3),
                                                             (3, 2), (3, 4),
                                                             (4, 6), (4, 7),
                                                             (5, 1),
                                                             (6, 3), (6, 7);

-- FACTURAS (una por pedido, sin factura para el pedido cancelado)
INSERT INTO facturas (id, numero_factura, fecha_emision, importe_total, pagada, metodo_pago, pedido_id) VALUES
                                                                                                            (1, 'FAC-2026-0001', '2026-09-05',  89.98, TRUE,  'Tarjeta',       1),
                                                                                                            (2, 'FAC-2026-0002', '2026-10-12',  69.99, TRUE,  'PayPal',        2),
                                                                                                            (3, 'FAC-2026-0003', '2026-11-03',  89.98, FALSE, NULL,            3),
                                                                                                            (4, 'FAC-2026-0004', '2026-10-20',  71.94, TRUE,  'Transferencia', 4),
                                                                                                            (5, 'FAC-2026-0005', '2026-11-18',  59.99, FALSE, NULL,            5);