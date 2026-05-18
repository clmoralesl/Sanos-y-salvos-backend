# Microservicio de Coincidencias (MS-COINCIDENCIAS)

Motor de reglas para emparejar reportes de mascotas perdidas con reportes de hallazgos.

## Lógica de Negocio
Utiliza el índice H3 (proximidad geoespacial) y metadatos de raza/especie para detectar posibles encuentros exitosos y notificar a los usuarios involucrados.

## Endpoints REST (/api/v1/coincidencias)
- POST /trigger/{reporteId}: Inicia el proceso de búsqueda de coincidencias para un reporte específico (Llamado internamente por MS-MASCOTAS).
- GET /perdida/{reporteId}: Obtiene la lista de posibles hallazgos que coinciden con un reporte de mascota perdida.
- GET /hallazgo/{reporteId}: Obtiene la lista de mascotas perdidas que coinciden con un reporte de hallazgo.

## Ejecución
Se integra mediante Feign con MS-MASCOTAS para recibir notificaciones automáticas de nuevos reportes.
