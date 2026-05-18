# Microservicio de Geolocalización (MS-GEO)

Especializado en la gestión de coordenadas e indexación H3.

## Tecnologías
- Java 17 / Spring Boot 3.2
- H3 Java (Uber): Para indexación hexagonal.
- MySQL 8.0: Almacenamiento de ubicaciones y zonas.

## Endpoints REST

### Ubicaciones (/geo/v1/ubicaciones)
- POST /: Registra una nueva coordenada geográfica (Requiere X-Auth0-Id). Retorna el ID de ubicación generado.
- GET /{id}: Obtiene los detalles de una ubicación (Coordenadas, Comuna, Región, Código H3).

### Catálogos Geográficos (/geo/v1/catalogos-geo)
- GET /regiones: Lista todas las regiones de Chile cargadas en el sistema.
- GET /regiones/{idRegion}/comunas: Lista todas las comunas pertenecientes a una región específica.

## Uso
Gestiona el registro de ubicaciones y el cálculo automático de celdas H3 para los reportes de mascotas, permitiendo búsquedas espaciales eficientes.
