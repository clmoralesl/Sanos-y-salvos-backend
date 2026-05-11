# Microservicio de Geolocalización (`ms-geo`) - Guía de Integración

Este documento detalla la estructura, responsabilidades y los endpoints del **Microservicio de Geolocalización (`ms-geo`)**. Está diseñado específicamente para servir de guía al equipo de Frontend y a los desarrolladores de `ms-mascotas` para la correcta integración y generación de reportes.

---

## 1. Visión General
`ms-geo` es responsable de gestionar toda la información espacial del ecosistema "Sanos y Salvos".
Opera bajo su propio esquema lógico de base de datos compuesto por 4 tablas principales:
- `REGION` y `COMUNA` (Catálogos estáticos).
- `UBICACION_REPORTE` (Tabla transaccional).
- `ZONA_GEO` (Índices espaciales Hexagonales usando Uber H3).

**Stack Tecnológico:** Java 17, Spring Boot 3.2.4, Maven, MySQL, Uber H3 Core.

---

## 2. Autenticación Temporal
Actualmente, el microservicio valida la identidad a través de cabeceras HTTP.
Todos los endpoints transaccionales (como el `POST`) **exigen** la inyección de la siguiente cabecera:
- **Header:** `X-Auth0-Id`
- **Valor:** El ID o subject del usuario autenticado en Auth0 (ej. `auth0|65f1a9b2c8e4d`).

---

## 3. Endpoints Disponibles

### A. Catálogos Geográficos (Lectura)

#### A.1 Obtener todas las Regiones
* **Ruta:** `GET /api/v1/catalogos-geo/regiones`
* **Descripción:** Lista todas las regiones de Chile pre-cargadas en el sistema.
* **Respuesta Exitosa (200 OK):**
```json
[
  { "id": 1, "nombre": "Región de Tarapacá" },
  { "id": 3, "nombre": "Región Metropolitana de Santiago" }
]
```

#### A.2 Obtener Comunas por Región
* **Ruta:** `GET /api/v1/catalogos-geo/regiones/{idRegion}/comunas`
* **Descripción:** Lista las comunas pertenecientes a una región específica indicando el ID en la URL.
* **Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Santiago",
    "region": { "id": 3, "nombre": "Región Metropolitana de Santiago" }
  }
]
```

---

### B. Gestión de Ubicaciones (Integración Principal)

Este endpoint es el **núcleo de integración** para generar un reporte.

#### B.1 Crear Ubicación de Reporte
* **Ruta:** `POST /api/v1/ubicaciones`
* **Headers:** `X-Auth0-Id: <usuario_id>`
* **Cuerpo de la Petición (JSON):**
```json
{
    "latitud": -33.4489,
    "longitud": -70.6693,
    "idComuna": 1
}
```
* **Qué hace internamente:** Recibe las coordenadas, utiliza la librería **Uber H3** para calcular automáticamente el polígono hexagonal (con una resolución de 9) e inserta/vincula esta ubicación en la tabla `UBICACION_REPORTE`.
* **Respuesta Exitosa (201 Created):** Retorna **únicamente** el ID generado (ej: `1` o `45`).

---

### C. Herramientas y Pruebas (Sandbox)

#### C.1 Calcular Índice H3 al Vuelo
* **Ruta:** `GET /api/v1/h3-test/indice?latitud={lat}&longitud={lng}&resolucion={res}`
* **Descripción:** Endpoint de utilería para corroborar en qué hexágono cae una coordenada sin registrarla en Base de Datos. Pasa la `latitud` y `longitud` por los Query Parameters.
* **Respuesta Exitosa (200 OK):**
```json
{
  "latitud": -33.4489,
  "longitud": -70.6693,
  "resolucion": 9,
  "indiceHexagonal": "8985c2d3083ffff"
}
```

---

## 4. Flujo de Integración Recomendado para la Creación de Reportes

De acuerdo a la arquitectura actual, el flujo de creación de un reporte desde el Frontend para orquestar `ms-geo` y `ms-mascotas` debe ser el siguiente:

1. **Petición a MS GEO:** El Frontend obtiene las coordenadas del mapa y llama al endpoint `POST /api/v1/ubicaciones` de `ms-geo`.
2. **Recepción del ID:** `ms-geo` procesa la coordenada geolocalizada y retorna el número de `id_ubicacion_reporte` (ej. `150`).
3. **Petición a MS MASCOTAS:** El Frontend arma el payload final del reporte (fotos, color, tipo de reporte, etc.) **incluyendo internamente la llave**: `"idUbicacionReporte": 150`.
4. **Persistencia Final:** El microservicio `ms-mascotas` guarda su entidad `Reporte` consolidando la llave foránea lógica hacia `ms-geo`.

*(Nota: En futuras iteraciones con API Gateway, este paso de orquestación puede trasladarse al Backend Pattern (BFF) o utilizando comunicación interna con OpenFeign entre microservicios).*

---

## 5. Configuración y Despliegue Local (Docker)

El proyecto cuenta con un entorno Dockerizado completo para agilizar el desarrollo y mantener un entorno aislado para la base de datos exclusiva de `ms-geo` (`sanos_salvos_geo_db`).

### Prerrequisitos
- Tener [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/) instalados en el sistema.

### Levantar el entorno
1. Ubícate en la raíz del proyecto `ms-geo` (donde se encuentra el `docker-compose.yml`).
2. Ejecuta el siguiente comando en la terminal:
   ```bash
   docker-compose up -d --build
   ```
3. Docker Compose creará y levantará dos contenedores:
   - **`geo-db`**: Una instancia de MySQL 8.0 expuesta en tu puerto local `3307` (Para no interferir con la BD de `ms-mascotas` en el `3306`).
   - **`ms-geo`**: El microservicio de geolocalización compilado y ejecutándose en el puerto `8082`.
4. El contenedor de `ms-geo` esperará a que MySQL esté saludable. Al iniciar, Hibernate creará las tablas y Spring Boot ejecutará automáticamente el archivo `src/main/resources/data.sql` para precargar las regiones y comunas en la base de datos vacía.

### Comandos útiles
- Ver logs del microservicio en tiempo real:
  ```bash
  docker logs -f sanos_salvos_ms_geo
  ```
- Bajar los contenedores:
  ```bash
  docker-compose down
  ```
- Bajar los contenedores y limpiar la base de datos (eliminar volúmenes):
  ```bash
  docker-compose down -v
  ```
