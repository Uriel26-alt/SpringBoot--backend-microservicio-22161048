# Microservicio de Tienda - Backend Spring Boot

Microservicio REST para gestión de productos y ventas desarrollado con Spring Boot 3.4.1 y Java 21.

## Tecnologías

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y CORS
- **PostgreSQL** - Base de datos
- **Swagger/OpenAPI** - Documentación de API
- **Lombok** - Reducción de boilerplate
- **Maven** - Gestión de dependencias

## Estructura del Proyecto

```
backend-microservicio/
├── src/
│   ├── main/
│   │   ├── java/com/microservicio/tienda/
│   │   │   ├── config/          # Configuraciones (Security, OpenAPI)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Manejo de excepciones
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios Spring Data
│   │   │   └── service/         # Lógica de negocio
│   │   └── resources/
│   │       └── application.yaml # Configuración de la aplicación
│   └── test/                    # Tests unitarios e integración
├── sql/                         # Scripts SQL
│   ├── 01_create_database.sql
│   ├── 02_create_tables.sql
│   └── 03_insert_sample_data.sql
└── pom.xml                      # Dependencias Maven
```

## Módulos

### 1. Productos
Gestión completa de productos del catálogo.

**Entidad:** `Producto`
- id (Long)
- nombre (String)
- descripcion (String)
- precio (BigDecimal)
- stock (Integer)
- activo (Boolean)

**Endpoints:**
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/activos` - Listar productos activos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar (desactivar) producto
- `GET /api/productos/buscar?nombre={nombre}` - Buscar por nombre

### 2. Ventas
Gestión de ventas con múltiples productos por venta.

**Entidades:**
- `Venta` (id, clienteNombre, clienteEmail, clienteTelefono, fechaVenta, total, estado)
- `DetalleVenta` (id, ventaId, productoId, cantidad, precioUnitario, subtotal)

**Endpoints:**
- `GET /api/ventas` - Listar todas las ventas
- `GET /api/ventas/{id}` - Obtener venta por ID
- `POST /api/ventas` - Crear nueva venta
- `PATCH /api/ventas/{id}/estado` - Actualizar estado de venta
- `DELETE /api/ventas/{id}` - Eliminar venta
- `GET /api/ventas/estado/{estado}` - Buscar por estado
- `GET /api/ventas/cliente?nombre={nombre}` - Buscar por cliente

**Estados de Venta:**
- PENDIENTE
- COMPLETADA
- CANCELADA

## Configuración de Base de Datos

### Prerequisitos
- PostgreSQL 12 o superior instalado

### Crear Base de Datos

```bash
# Ejecutar scripts en orden:
psql -U postgres -f sql/01_create_database.sql
psql -U postgres -f sql/02_create_tables.sql
psql -U postgres -f sql/03_insert_sample_data.sql
```

### Configuración en application.yaml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tienda_db
    username: postgres
    password: postgres
```

## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
cd backend-microservicio
```

### 2. Configurar base de datos

Editar `src/main/resources/application.yaml` con tus credenciales de PostgreSQL.

### 3. Compilar el proyecto

```bash
./mvnw clean install
```

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## Documentación API

Una vez iniciada la aplicación, acceder a:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## Ejemplos de Uso

### Crear un Producto

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Dell",
    "descripcion": "Laptop Dell Inspiron 15",
    "precio": 15000.00,
    "stock": 10,
    "activo": true
  }'
```

### Crear una Venta

```bash
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteNombre": "Juan Pérez",
    "clienteEmail": "juan@email.com",
    "clienteTelefono": "555-1234",
    "estado": "COMPLETADA",
    "detalles": [
      {
        "productoId": 1,
        "cantidad": 2,
        "precioUnitario": 1899.00
      }
    ]
  }'
```

## Características

- **CRUD completo** para productos y ventas
- **Validación de datos** con Bean Validation
- **Manejo de stock** automático en ventas
- **Soft delete** en productos
- **Control de estados** en ventas
- **Búsquedas y filtros** por múltiples criterios
- **Documentación automática** con Swagger
- **CORS configurado** para frontend
- **Manejo global de excepciones**
- **Logs SQL** para debugging

## Testing

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar con cobertura
./mvnw test jacoco:report
```

## Puertos

- **Aplicación:** 8080
- **Base de datos:** 5432 (PostgreSQL)

## Seguridad

- CORS habilitado para localhost:5173, localhost:3000
- Endpoints `/api/**` públicos
- Actuator endpoints protegidos
- Usuario admin: admin/admin123

## Autor

Microservicio desarrollado para actividad de Spring Boot
