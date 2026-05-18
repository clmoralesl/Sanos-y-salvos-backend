# Backend For Frontend (BFF) - Sanos y Salvos

Servicio orquestador que centraliza y simplifica la comunicación entre el Frontend y los Microservicios internos.

## Funcionalidades
- Agregación de Datos: Combina información de ms-mascotas y ms-geo en una sola respuesta.
- Simplificación de Payload: Envía solo los datos necesarios para la vista, reduciendo el consumo de red.
- Abstracción: Oculta la complejidad de la red interna de microservicios.

## Endpoints REST (/bff/v1/reportes)
- GET /{id}/detalle: Obtiene el detalle completo orquestado de un reporte (Usuario + Mascota + Ubicación + Metadata H3). Requiere X-Auth0-Id.

## Instalación y Ejecución
Requiere que los microservicios ms-mascotas y ms-geo estén en ejecución.
```bash
mvn spring-boot:run
```
