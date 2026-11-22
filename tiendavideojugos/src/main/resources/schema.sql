-- =====================================================================
-- GameStore - schema.sql (esquema completo del grupo: 8 entidades + tabla intermedia)
-- Compatible con MariaDB/MySQL y H2 (MODE=MySQL).
-- Se ejecuta en cada arranque: primero se borran las tablas (de las
-- dependientes a las independientes) y luego se vuelven a crear.
-- Los nombres de tabla/columna coinciden con las anotaciones @Table y @Column de las entidades.
-- =====================================================================

DROP TABLE IF EXISTS facturas;
DROP TABLE IF EXISTS pedido_videojuego;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS videojuego;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS ficha_tecnica;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS plataforma;
DROP TABLE IF EXISTS desarrolladora;

-- ---------------------------------------------------------------------
-- Categoria, FichaTecnica
-- ---------------------------------------------------------------------
CREATE TABLE categoria (
                           id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre      VARCHAR(60)  NOT NULL UNIQUE,
                           descripcion VARCHAR(300) NOT NULL,
                           activa      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE ficha_tecnica (
                               id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                               sistema_operativo VARCHAR(80)  NOT NULL,
                               procesador        VARCHAR(100) NOT NULL,
                               memoria_ram_gb    INT          NOT NULL,
                               espacio_disco_gb  INT          NOT NULL,
                               requiere_internet BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ---------------------------------------------------------------------
-- Plataforma, Desarrolladora
-- ---------------------------------------------------------------------
CREATE TABLE plataforma (
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            nombre            VARCHAR(100) NOT NULL UNIQUE,
                            fabricante        VARCHAR(100) NOT NULL,
                            generacion        VARCHAR(100) NOT NULL,
                            fecha_lanzamiento VARCHAR(100) NOT NULL,
                            es_portatil       BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE desarrolladora (
                                id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                nombre          VARCHAR(100) NOT NULL UNIQUE,
                                pais_origen     VARCHAR(100) NOT NULL,
                                fecha_fundacion DATE,
                                pagina_web      VARCHAR(200)
);

-- ---------------------------------------------------------------------
-- Videojuego (ManyToOne con Categoria, Plataforma y Desarrolladora; OneToOne con FichaTecnica)
-- ---------------------------------------------------------------------
CREATE TABLE videojuego (
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            titulo            VARCHAR(120)  NOT NULL UNIQUE,
                            descripcion       VARCHAR(1000) NOT NULL,
                            precio            DECIMAL(8,2)  NOT NULL,
                            stock             INT           NOT NULL,
                            fecha_lanzamiento DATE          NOT NULL,
                            imagen            VARCHAR(255),
                            es_multijugador   BOOLEAN       NOT NULL DEFAULT FALSE,
                            categoria_id      BIGINT        NOT NULL,
                            ficha_tecnica_id  BIGINT        UNIQUE,
                            plataforma_id     BIGINT,
                            desarrolladora_id BIGINT,
                            CONSTRAINT fk_videojuego_categoria      FOREIGN KEY (categoria_id)      REFERENCES categoria (id),
                            CONSTRAINT fk_videojuego_ficha          FOREIGN KEY (ficha_tecnica_id)  REFERENCES ficha_tecnica (id),
                            CONSTRAINT fk_videojuego_plataforma     FOREIGN KEY (plataforma_id)     REFERENCES plataforma (id),
                            CONSTRAINT fk_videojuego_desarrolladora FOREIGN KEY (desarrolladora_id) REFERENCES desarrolladora (id)
);

-- ---------------------------------------------------------------------
-- Cliente (tabla "clientes", como en @Table de la entidad)
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
-- Pedido (tabla "pedidos"): ManyToOne con Cliente
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
-- Tabla intermedia ManyToMany Pedido-Videojuego
-- ---------------------------------------------------------------------
CREATE TABLE pedido_videojuego (
                                   pedido_id     BIGINT NOT NULL,
                                   videojuego_id BIGINT NOT NULL,
                                   PRIMARY KEY (pedido_id, videojuego_id),
                                   CONSTRAINT fk_pv_pedido     FOREIGN KEY (pedido_id)     REFERENCES pedidos (id),
                                   CONSTRAINT fk_pv_videojuego FOREIGN KEY (videojuego_id) REFERENCES videojuego (id)
);

-- ---------------------------------------------------------------------
-- Factura (tabla "facturas"): OneToOne con Pedido (la FK está aquí, en pedido_id)
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