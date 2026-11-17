-- =================================================================
-- Script de creación de tablas
-- Microservicio de Tienda - Productos y Ventas
-- =================================================================

-- Conectarse a la base de datos
\c tienda_db;

-- =================================================================
-- TABLA: productos
-- Descripción: Almacena la información de los productos disponibles
-- =================================================================
CREATE TABLE IF NOT EXISTS productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio NUMERIC(10, 2) NOT NULL CHECK (precio > 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    activo BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT uk_producto_nombre UNIQUE (nombre)
);

COMMENT ON TABLE productos IS 'Tabla de productos del catálogo';
COMMENT ON COLUMN productos.id IS 'Identificador único del producto';
COMMENT ON COLUMN productos.nombre IS 'Nombre del producto (único)';
COMMENT ON COLUMN productos.descripcion IS 'Descripción detallada del producto';
COMMENT ON COLUMN productos.precio IS 'Precio unitario del producto';
COMMENT ON COLUMN productos.stock IS 'Cantidad disponible en inventario';
COMMENT ON COLUMN productos.activo IS 'Indica si el producto está activo (soft delete)';

-- =================================================================
-- TABLA: ventas
-- Descripción: Almacena las ventas realizadas
-- =================================================================
CREATE TABLE IF NOT EXISTS ventas (
    id BIGSERIAL PRIMARY KEY,
    cliente_nombre VARCHAR(100) NOT NULL,
    cliente_email VARCHAR(100),
    cliente_telefono VARCHAR(20),
    fecha_venta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
    CONSTRAINT chk_venta_estado CHECK (estado IN ('PENDIENTE', 'COMPLETADA', 'CANCELADA'))
);

COMMENT ON TABLE ventas IS 'Tabla de ventas realizadas';
COMMENT ON COLUMN ventas.id IS 'Identificador único de la venta';
COMMENT ON COLUMN ventas.cliente_nombre IS 'Nombre del cliente que realiza la compra';
COMMENT ON COLUMN ventas.cliente_email IS 'Email de contacto del cliente';
COMMENT ON COLUMN ventas.cliente_telefono IS 'Teléfono de contacto del cliente';
COMMENT ON COLUMN ventas.fecha_venta IS 'Fecha y hora de la venta';
COMMENT ON COLUMN ventas.total IS 'Monto total de la venta';
COMMENT ON COLUMN ventas.estado IS 'Estado de la venta: PENDIENTE, COMPLETADA, CANCELADA';

-- =================================================================
-- TABLA: detalle_ventas
-- Descripción: Almacena los productos incluidos en cada venta
-- =================================================================
CREATE TABLE IF NOT EXISTS detalle_ventas (
    id BIGSERIAL PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10, 2) NOT NULL CHECK (precio_unitario > 0),
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id)
        REFERENCES ventas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id)
        REFERENCES productos(id) ON DELETE RESTRICT
);

COMMENT ON TABLE detalle_ventas IS 'Tabla de detalles de cada venta (productos vendidos)';
COMMENT ON COLUMN detalle_ventas.id IS 'Identificador único del detalle';
COMMENT ON COLUMN detalle_ventas.venta_id IS 'Referencia a la venta';
COMMENT ON COLUMN detalle_ventas.producto_id IS 'Referencia al producto vendido';
COMMENT ON COLUMN detalle_ventas.cantidad IS 'Cantidad de unidades vendidas';
COMMENT ON COLUMN detalle_ventas.precio_unitario IS 'Precio unitario al momento de la venta';
COMMENT ON COLUMN detalle_ventas.subtotal IS 'Subtotal del detalle (cantidad * precio_unitario)';

-- =================================================================
-- ÍNDICES para mejorar el rendimiento
-- =================================================================

-- Índices en productos
CREATE INDEX idx_producto_nombre ON productos(nombre);
CREATE INDEX idx_producto_activo ON productos(activo);

-- Índices en ventas
CREATE INDEX idx_venta_fecha ON ventas(fecha_venta);
CREATE INDEX idx_venta_estado ON ventas(estado);
CREATE INDEX idx_venta_cliente ON ventas(cliente_nombre);

-- Índices en detalle_ventas
CREATE INDEX idx_detalle_venta_id ON detalle_ventas(venta_id);
CREATE INDEX idx_detalle_producto_id ON detalle_ventas(producto_id);

-- =================================================================
-- FIN DEL SCRIPT
-- =================================================================
