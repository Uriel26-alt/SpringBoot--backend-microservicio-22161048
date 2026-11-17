-- =================================================================
-- Script de creación de base de datos
-- Microservicio de Tienda - Productos y Ventas
-- =================================================================

-- Crear la base de datos (ejecutar como usuario postgres)
CREATE DATABASE tienda_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'es_ES.UTF-8'
    LC_CTYPE = 'es_ES.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE tienda_db IS 'Base de datos para el microservicio de gestión de productos y ventas';

-- Conectarse a la base de datos
\c tienda_db;
