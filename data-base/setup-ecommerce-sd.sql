-- Ejecutar el archivo completo desde una consola Database de IntelliJ
-- conectada al MySQL local con un usuario administrador (por ejemplo, root).
-- Es seguro volver a ejecutarlo: no elimina tablas ni datos existentes.

CREATE DATABASE IF NOT EXISTS `e-commerce-sierra-dorada`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

ALTER DATABASE `e-commerce-sierra-dorada`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ecommerce_sd'@'localhost' IDENTIFIED BY 'Sierra2026*';
ALTER USER 'ecommerce_sd'@'localhost' IDENTIFIED BY 'Sierra2026*';
GRANT ALL PRIVILEGES ON `e-commerce-sierra-dorada`.* TO 'ecommerce_sd'@'localhost';

CREATE USER IF NOT EXISTS 'ecommerce_sd'@'127.0.0.1' IDENTIFIED BY 'Sierra2026*';
ALTER USER 'ecommerce_sd'@'127.0.0.1' IDENTIFIED BY 'Sierra2026*';
GRANT ALL PRIVILEGES ON `e-commerce-sierra-dorada`.* TO 'ecommerce_sd'@'127.0.0.1';

USE `e-commerce-sierra-dorada`;

CREATE TABLE IF NOT EXISTS metodos_pago (
    id_metodo_pago INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(10),
    direccion TEXT,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    rol ENUM('CLIENTE', 'ADMIN') NOT NULL DEFAULT 'CLIENTE'
);

CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    categoria_principal_id INT NULL,
    CONSTRAINT fk_categoria_principal
        FOREIGN KEY (categoria_principal_id) REFERENCES categorias(id_categoria)
);

CREATE TABLE IF NOT EXISTS productos (
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre_produ VARCHAR(200) NOT NULL,
    descripcion_produ TEXT,
    precio_base DECIMAL(12,2) NOT NULL,
    categoria_id INT,
    marca VARCHAR(100),
    tipo_cerveza VARCHAR(100),
    estilo_cerveza VARCHAR(100),
    stock INT DEFAULT 0,
    abv DECIMAL(4,2),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria)
);

CREATE TABLE IF NOT EXISTS pedidos (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_pedido DECIMAL(12,2) NOT NULL,
    estado ENUM(
        'Pendiente',
        'Confirmado',
        'En preparación',
        'Enviado',
        'Entregado',
        'Cancelado'
    ) DEFAULT 'Pendiente',
    direccion_envio TEXT NOT NULL,
    metodo_pago_id INT,
    fecha_confirmacion DATETIME,
    fecha_envio DATETIME,
    fecha_entrega DATETIME,
    notas TEXT,
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_pedido_metodo_pago
        FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id_metodo_pago),
    CONSTRAINT chk_total_pedido CHECK (total_pedido >= 0)
);

CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);

CREATE TABLE IF NOT EXISTS pagos (
    id_pagos INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    metodo_pago_id INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('Pendiente', 'Completado', 'Fallido', 'Reembolsado') DEFAULT 'Pendiente',
    transaccion_id VARCHAR(100),
    CONSTRAINT fk_pago_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    CONSTRAINT fk_pago_metodo
        FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id_metodo_pago)
);

FLUSH PRIVILEGES;

SELECT CURRENT_USER() AS ejecutado_por,
       DATABASE() AS base_activa;

SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'e-commerce-sierra-dorada'
ORDER BY table_name;
