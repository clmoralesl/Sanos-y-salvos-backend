# Microservicio de Mascotas (MS-MASCOTAS)

Gestiona el núcleo de la lógica de negocio: Usuarios, Mascotas y Reportes.

## Stack Tecnológico
- Java 17
- Spring Boot 3.2.4
- MySQL 8.0
- Hibernate / JPA

## Ejecución con Docker
Este servicio se levanta automáticamente mediante el Docker Compose raíz del proyecto. Para levantarlo individualmente:
```bash
docker-compose up ms-mascotas
```

## Endpoints REST

### Reportes (/mascotas/v1/reportes)
- POST /: Crea un nuevo reporte (Requiere X-Auth0-Id).
- GET /: Lista todos los reportes.
- GET /{id}: Obtiene un reporte por ID.
- PUT /{id}: Actualiza un reporte existente (Requiere X-Auth0-Id).
- PUT /{id}/cerrar: Marca un reporte como resuelto (Requiere X-Auth0-Id).
- DELETE /{id}: Elimina un reporte (Requiere X-Auth0-Id).

### Mascotas (/mascotas/v1/mascotas)
- POST /: Registra una nueva mascota (Requiere X-Auth0-Id).
- GET /: Lista todas las mascotas del sistema.
- GET /me: Lista las mascotas del usuario autenticado (Requiere X-Auth0-Id).
- GET /{id}: Obtiene detalles de una mascota por ID.
- PUT /{id}: Actualiza datos de una mascota (Requiere X-Auth0-Id).
- DELETE /{id}: Elimina una mascota.

### Usuarios (/mascotas/v1/usuarios)
- POST /registro: Registra un nuevo usuario en la plataforma.
- GET /me: Obtiene el perfil del usuario actual (Requiere X-Auth0-Id).
- PUT /me: Actualiza el perfil actual (Requiere X-Auth0-Id).
- DELETE /me: Elimina la cuenta del usuario actual (Requiere X-Auth0-Id).
- GET /: Lista todos los usuarios (Admin).
- GET /{id}: Obtiene un usuario por ID (Admin).
- PUT /{id}: Actualiza un usuario por ID (Admin).
- DELETE /{id}: Elimina un usuario por ID (Admin).

### Catálogos (/mascotas/v1/catalogos)
- GET /razas: Lista de razas disponibles.
- GET /tamanios: Lista de tamaños (Pequeño, Mediano, Grande).
- GET /caracteristicas: Lista de rasgos físicos.
- GET /tipos-reporte: Tipos de incidencia (Perdida, Hallazgo).
- GET /tipos-cuenta: Roles de usuario.

## Pruebas
```bash
mvn test
```
