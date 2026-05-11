# Sanos y Salvos - Ecosistema Backend 🐾

Este repositorio es un monorepo que contiene toda la lógica de backend del ecosistema **Sanos y Salvos**. 
Consiste en una serie de microservicios con sus propias bases de datos y un API Gateway como único punto de entrada público.

## Arquitectura

Para detalles exhaustivos, revisa el archivo de arquitectura base en: `api-gateway/ARQUITECTURA_ECOSISTEMA.md` o el de los respectivos módulos. 

### Microservicios:
1. **API Gateway (`api-gateway`):**
   - Puerto Externo: `8080`
   - Función: Enrutamiento, rate limiting y validación de seguridad (Auth0).
2. **Microservicio Mascotas (`ms-mascotas`):**
   - Base de Datos: `mysql-mascotas` (MySQL 8.0)
   - Función: Gestión de reportes de pérdida y hallazgo de mascotas.
3. **Microservicio Geo (`ms-geo`):**
   - Base de Datos: `mysql-geo` (MySQL 8.0)
   - Función: Resolución espacial e índices hexagonales (Uber H3).
4. **Motor de Coincidencias (`ms-coincidencias`):**
   - Base de Datos: `mysql-coincidencias` (MySQL 8.0)
   - Función: Calcula qué reporte de mascota perdida podría corresponder a uno de hallazgo basado en características físicas y un umbral de coincidencia.

## Requisitos
- **Java 17**
- **Maven**
- **Docker & Docker Compose**

## Cómo Iniciar el Entorno Localmente 🚀

Gracias a la centralización, puedes compilar todo y levantar todo el ecosistema con Docker usando la red unificada.

1. **(Opcional) Compilar localmente todos los proyectos Java simultáneamente:**
   ```bash
   mvn clean package -DskipTests
   ```
2. **Levantar todos los microservicios y bases de datos usando Docker Compose:**
   ```bash
   docker-compose up -d --build
   ```

3. **Verificar que el sistema está corriendo:**
   - Gateway: `http://localhost:8080`
   - Puedes checar los logs de cada servicio si algo falla para conectarse:
     `docker-compose logs -f ms-coincidencias`

## CI/CD ⚙️

El proyecto incluye un action en GitHub para compilar todo desde la raíz (`.github/workflows/main.yml`) verificando que todos los microservicios son sanos y desplegables cada vez que modificas la rama `main`.
