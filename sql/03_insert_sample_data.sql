-- =================================================================
-- Script de inserción de datos de ejemplo
-- Microservicio de Tienda - Productos y Ventas
-- =================================================================

\c tienda_db;

-- =================================================================
-- INSERTAR PRODUCTOS DE EJEMPLO
-- =================================================================

INSERT INTO productos (nombre, descripcion, precio, stock, activo) VALUES
('Laptop HP Pavilion', 'Laptop HP Pavilion 15.6", Intel Core i5, 8GB RAM, 256GB SSD', 12500.00, 15, true),
('Mouse Logitech MX Master', 'Mouse inalámbrico ergonómico con 7 botones programables', 1899.00, 50, true),
('Teclado Mecánico Corsair', 'Teclado mecánico RGB con switches Cherry MX Red', 2499.00, 30, true),
('Monitor Samsung 27"', 'Monitor LED Full HD 27 pulgadas, 75Hz, FreeSync', 4500.00, 20, true),
('Webcam Logitech C920', 'Webcam HD 1080p con micrófono estéreo integrado', 1299.00, 40, true),
('Audífonos Sony WH-1000XM4', 'Audífonos inalámbricos con cancelación de ruido activa', 6999.00, 25, true),
('SSD Kingston 1TB', 'Unidad de estado sólido NVMe 1TB, velocidad lectura 3500MB/s', 1899.00, 60, true),
('Memoria RAM Corsair 16GB', 'Memoria DDR4 16GB (2x8GB) 3200MHz RGB', 1599.00, 45, true),
('Router TP-Link Archer', 'Router inalámbrico Dual Band AC1750, 1300Mbps', 1199.00, 35, true),
('Impresora HP LaserJet', 'Impresora láser monocromática, 28ppm, WiFi', 3499.00, 12, true);

-- =================================================================
-- INSERTAR VENTAS DE EJEMPLO
-- =================================================================

-- Venta 1: Juan Pérez compra una Laptop y Mouse
INSERT INTO ventas (cliente_nombre, cliente_email, cliente_telefono, fecha_venta, total, estado)
VALUES ('Juan Pérez', 'juan.perez@email.com', '555-1234', NOW() - INTERVAL '5 days', 14399.00, 'COMPLETADA');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 12500.00, 12500.00),
(1, 2, 1, 1899.00, 1899.00);

-- Venta 2: María González compra componentes de PC
INSERT INTO ventas (cliente_nombre, cliente_email, cliente_telefono, fecha_venta, total, estado)
VALUES ('María González', 'maria.gonzalez@email.com', '555-5678', NOW() - INTERVAL '3 days', 7097.00, 'COMPLETADA');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(2, 3, 1, 2499.00, 2499.00),
(2, 7, 2, 1899.00, 3798.00),
(2, 8, 1, 1599.00, 1599.00);

-- Venta 3: Carlos Ramírez - venta pendiente
INSERT INTO ventas (cliente_nombre, cliente_email, cliente_telefono, fecha_venta, total, estado)
VALUES ('Carlos Ramírez', 'carlos.ramirez@email.com', '555-9012', NOW() - INTERVAL '1 day', 11499.00, 'PENDIENTE');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(3, 4, 1, 4500.00, 4500.00),
(3, 6, 1, 6999.00, 6999.00);

-- Venta 4: Ana Martínez - oficina en casa
INSERT INTO ventas (cliente_nombre, cliente_email, cliente_telefono, fecha_venta, total, estado)
VALUES ('Ana Martínez', 'ana.martinez@email.com', '555-3456', NOW() - INTERVAL '2 days', 10297.00, 'COMPLETADA');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(4, 5, 1, 1299.00, 1299.00),
(4, 9, 1, 1199.00, 1199.00),
(4, 10, 1, 3499.00, 3499.00),
(4, 4, 1, 4500.00, 4500.00);

-- Venta 5: Roberto Silva - accesorios
INSERT INTO ventas (cliente_nombre, cliente_email, cliente_telefono, fecha_venta, total, estado)
VALUES ('Roberto Silva', 'roberto.silva@email.com', NULL, NOW(), 3498.00, 'COMPLETADA');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(5, 2, 1, 1899.00, 1899.00),
(5, 8, 1, 1599.00, 1599.00);

-- =================================================================
-- ACTUALIZAR STOCK DE PRODUCTOS SEGÚN LAS VENTAS
-- =================================================================

UPDATE productos SET stock = stock - 1 WHERE id = 1; -- Laptop (1 vendida)
UPDATE productos SET stock = stock - 2 WHERE id = 2; -- Mouse (2 vendidos)
UPDATE productos SET stock = stock - 1 WHERE id = 3; -- Teclado (1 vendido)
UPDATE productos SET stock = stock - 3 WHERE id = 4; -- Monitor (3 vendidos)
UPDATE productos SET stock = stock - 1 WHERE id = 5; -- Webcam (1 vendida)
UPDATE productos SET stock = stock - 1 WHERE id = 6; -- Audífonos (1 vendido)
UPDATE productos SET stock = stock - 2 WHERE id = 7; -- SSD (2 vendidos)
UPDATE productos SET stock = stock - 2 WHERE id = 8; -- RAM (2 vendidas)
UPDATE productos SET stock = stock - 1 WHERE id = 9; -- Router (1 vendido)
UPDATE productos SET stock = stock - 1 WHERE id = 10; -- Impresora (1 vendida)

-- =================================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- =================================================================

SELECT 'Productos insertados:' AS mensaje, COUNT(*) AS total FROM productos;
SELECT 'Ventas insertadas:' AS mensaje, COUNT(*) AS total FROM ventas;
SELECT 'Detalles de venta insertados:' AS mensaje, COUNT(*) AS total FROM detalle_ventas;

-- =================================================================
-- FIN DEL SCRIPT
-- =================================================================
