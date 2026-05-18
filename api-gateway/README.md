# API Gateway - Sanos y Salvos

Punto de entrada único para el ecosistema de microservicios, encargado de centralizar el tráfico y gestionar la seguridad transversal.

## Funciones
- Ruteo Dinámico: Redirige las peticiones según el prefijo (/api/mascotas/**, /api/geo/**, /api/bff/**).
- CORS Centralizado: Gestiona los permisos de origen para el Frontend (localhost:3000).
- Seguridad: Validación y propagación de cabeceras de identidad.

## Mapeo de Rutas (Gateway)
- /api/mascotas/** -> Redirige a ms-mascotas (Puerto 8081)
- /api/geo/** -> Redirige a ms-geo (Puerto 8082)
- /api/bff/** -> Redirige a bff-sanosysalvos (Puerto 8084)
- /api/coincidencias/** -> Redirige a ms-coincidencias (Puerto 8083)
