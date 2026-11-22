-- =====================================================================
-- schema.sql - Tienda de videojuegos
-- Compatible con MySQL/MariaDB y H2.
-- Orden de borrado: de las tablas dependientes a las independientes.
-- =====================================================================

DROP TABLE IF EXISTS pedido_videojuego;
DROP TABLE IF EXISTS facturas;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS videojuego;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS plataforma;
DROP TABLE IF EXISTS desarrolladora;

-- ---------------------------------------------------------------------
-- DESARROLLADORA (5 campos)
-- ---------------------------------------------------------------------
CREATE TABLE desarrolladora (
                                id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                nombre          VARCHAR(100) NOT NULL UNIQUE,
                                pais_origen     VARCHAR(100) NOT NULL,
                                fecha_fundacion DATE,
                                pagina_web      VARCHAR(200)
);

-- ---------------------------------------------------------------------
-- PLATAFORMA (6 campos)
-- ---------------------------------------------------------------------
CREATE TABLE plataforma (
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            nombre            VARCHAR(100) NOT NULL UNIQUE,
                            fabricante        VARCHAR(100) NOT NULL,
                            generacion        VARCHAR(100) NOT NULL,
                            fecha_lanzamiento VARCHAR(100) NOT NULL,
                            es_portatil       BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ---------------------------------------------------------------------
-- VIDEOJUEGO (8 campos)
-- OJO: la entidad Videojuego no estaba entre los archivos subidos.
-- Ajusta nombres/tipos de columnas a los de tu entidad real.
-- ---------------------------------------------------------------------
CREATE TABLE videojuego (
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            titulo            VARCHAR(150)  NOT NULL,
                            genero            VARCHAR(50)   NOT NULL,
                            fecha_lanzamiento DATE          NOT NULL,
                            precio            DECIMAL(10,2) NOT NULL,
                            stock             INT           NOT NULL DEFAULT 0,
                            desarrolladora_id BIGINT        NOT NULL,
                            plataforma_id     BIGINT        NOT NULL,
                            CONSTRAINT fk_videojuego_desarrolladora FOREIGN KEY (desarrolladora_id) REFERENCES desarrolladora (id),
                            CONSTRAINT fk_videojuego_plataforma     FOREIGN KEY (plataforma_id)     REFERENCES plataforma (id)
);

-- ---------------------------------------------------------------------
-- CLIENTES (9 campos)
-- ---------------------------------------------------------------------
CREATE TABLE clientes (
                          id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre         VARCHAR(50)  NOT NULL,
                          apellidos      VARCHAR(100) NOT NULL,
                          dni            VARCHAR(9)   NOT NULL UNIQUE,
                          correo         VARCHAR(100) NOT NULL UNIQUE,
                          telefono       VARCHAR(16)  NOT NULL,
                          fecha_registro DATE         NOT NULL,
                          direccion      VARCHAR(200),
                          es_premium     BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ---------------------------------------------------------------------
-- PEDIDOS (7 campos)
-- ---------------------------------------------------------------------
CREATE TABLE pedidos (
                         id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
                         fecha              DATE          NOT NULL,
                         precio_total       DECIMAL(10,2) NOT NULL,
                         estado             VARCHAR(20)   NOT NULL,
                         direccion_envio    VARCHAR(200)  NOT NULL,
                         numero_seguimiento VARCHAR(50),
                         cliente_id         BIGINT        NOT NULL,
                         CONSTRAINT fk_pedidos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

-- ---------------------------------------------------------------------
-- PEDIDO_VIDEOJUEGO (tabla intermedia N:M)
-- ---------------------------------------------------------------------
CREATE TABLE pedido_videojuego (
                                   pedido_id     BIGINT NOT NULL,
                                   videojuego_id BIGINT NOT NULL,
                                   PRIMARY KEY (pedido_id, videojuego_id),
                                   CONSTRAINT fk_pv_pedido     FOREIGN KEY (pedido_id)     REFERENCES pedidos (id),
                                   CONSTRAINT fk_pv_videojuego FOREIGN KEY (videojuego_id) REFERENCES videojuego (id)
);

-- ---------------------------------------------------------------------
-- FACTURAS (7 campos) - relación 1:1 con pedidos
-- ---------------------------------------------------------------------
CREATE TABLE facturas (
                          id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                          numero_factura VARCHAR(30)   NOT NULL UNIQUE,
                          fecha_emision  DATE          NOT NULL,
                          importe_total  DECIMAL(10,2) NOT NULL,
                          pagada         BOOLEAN       NOT NULL DEFAULT FALSE,
                          metodo_pago    VARCHAR(50),
                          pedido_id      BIGINT        NOT NULL UNIQUE,
                          CONSTRAINT fk_facturas_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos (id)
);