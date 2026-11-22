-- =====================================================================
-- GameStore - schema.sql (esquema completo del grupo: 8 entidades + tabla intermedia)
-- Compatible con H2 (MODE=MySQL) y MySQL 8.
-- Se ejecuta en cada arranque: primero se borran las tablas (en orden inverso a las
-- dependencias) y luego se vuelven a crear.
-- =====================================================================

DROP TABLE IF EXISTS pedido_videojuego;
DROP TABLE IF EXISTS videojuego;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS desarrolladora;
DROP TABLE IF EXISTS plataforma;
DROP TABLE IF EXISTS ficha_tecnica;
DROP TABLE IF EXISTS categoria;

-- ---------------------------------------------------------------------
-- PERSONA 1 (Héctor): Categoria, FichaTecnica, Videojuego
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
-- PERSONA 3 (Darío): Plataforma, Desarrolladora
-- ---------------------------------------------------------------------
CREATE TABLE plataforma (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(80) NOT NULL UNIQUE,
    fabricante        VARCHAR(80) NOT NULL,
    generacion        INT         NOT NULL,
    fecha_lanzamiento DATE        NOT NULL,
    es_portatil       BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE desarrolladora (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL UNIQUE,
    pais             VARCHAR(60)  NOT NULL,
    fecha_fundacion  DATE         NOT NULL,
    pagina_web       VARCHAR(200)
);

-- ---------------------------------------------------------------------
-- PERSONA 2 (Julio): Cliente, Factura, Pedido
-- ---------------------------------------------------------------------
CREATE TABLE cliente (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(60)  NOT NULL,
    apellidos      VARCHAR(100) NOT NULL,
    dni            VARCHAR(9)   NOT NULL UNIQUE,
    correo         VARCHAR(120) NOT NULL UNIQUE,
    telefono       VARCHAR(15)  NOT NULL,
    fecha_registro DATE         NOT NULL,
    direccion      VARCHAR(200) NOT NULL,
    es_premium     BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE factura (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero        VARCHAR(20)   NOT NULL UNIQUE,
    fecha_emision DATE          NOT NULL,
    importe       DECIMAL(10,2) NOT NULL
);

CREATE TABLE pedido (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha              DATETIME      NOT NULL,
    precio_total       DECIMAL(10,2) NOT NULL,
    estado             VARCHAR(30)   NOT NULL,
    direccion_envio    VARCHAR(200)  NOT NULL,
    numero_seguimiento VARCHAR(40),
    cliente_id         BIGINT        NOT NULL,
    factura_id         BIGINT        UNIQUE,                  -- OneToOne Pedido-Factura
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_pedido_factura FOREIGN KEY (factura_id) REFERENCES factura (id)
);

-- ---------------------------------------------------------------------
-- Videojuego (relacionada con todas las demás)
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
    categoria_id      BIGINT        NOT NULL,                 -- ManyToOne Videojuego-Categoria
    ficha_tecnica_id  BIGINT        UNIQUE,                   -- OneToOne  Videojuego-FichaTecnica
    plataforma_id     BIGINT,                                 -- ManyToOne Videojuego-Plataforma (Darío)
    desarrolladora_id BIGINT,                                 -- ManyToOne Videojuego-Desarrolladora (Darío)
    CONSTRAINT fk_videojuego_categoria     FOREIGN KEY (categoria_id)      REFERENCES categoria (id),
    CONSTRAINT fk_videojuego_ficha         FOREIGN KEY (ficha_tecnica_id)  REFERENCES ficha_tecnica (id),
    CONSTRAINT fk_videojuego_plataforma    FOREIGN KEY (plataforma_id)     REFERENCES plataforma (id),
    CONSTRAINT fk_videojuego_desarrolladora FOREIGN KEY (desarrolladora_id) REFERENCES desarrolladora (id)
);

-- ---------------------------------------------------------------------
-- Tabla intermedia ManyToMany Pedido-Videojuego (Julio)
-- ---------------------------------------------------------------------
CREATE TABLE pedido_videojuego (
    pedido_id     BIGINT NOT NULL,
    videojuego_id BIGINT NOT NULL,
    PRIMARY KEY (pedido_id, videojuego_id),
    CONSTRAINT fk_pv_pedido     FOREIGN KEY (pedido_id)     REFERENCES pedido (id),
    CONSTRAINT fk_pv_videojuego FOREIGN KEY (videojuego_id) REFERENCES videojuego (id)
);
