# 🛡️ Reporte de Cobertura de Pruebas (Backend)

Este documento describe la estrategia de calidad y pruebas automatizadas del ecosistema de microservicios de **Sanos y Salvos**, diseñado en Spring Boot.

## 1. Stack Tecnológico de Testing

Para garantizar el funcionamiento de los sistemas distribuidos, empleamos el siguiente stack:
- **JUnit 5 (Jupiter):** Framework principal para estructurar y ejecutar los ciclos de vida de las pruebas.
- **Mockito:** Librería de mockeo utilizada para aislar capas de la aplicación (ej. probar un Controlador sin necesidad de la Base de Datos real).
- **Spring Boot Test:** Provee anotaciones avanzadas como `@WebMvcTest` y `@DataJpaTest` para levantar contextos ligeros de Spring.
- **JaCoCo:** Plugin de Maven encargado de la instrumentación del bytecode en tiempo de ejecución para generar los reportes oficiales de cobertura.

## 2. Alcance y Estrategia de Cobertura

Se ha establecido la meta obligatoria de mantener al menos un **75%** de cobertura en las funciones principales, focalizando los esfuerzos en donde residen las reglas de negocio críticas.

### ¿Qué se está probando?
1. **Capa de Lógica de Negocio (Servicios):**
   - Clases como `MascotaServiceImpl` o `CoincidenciaServiceImpl`.
   - **Cómo se prueban:** Se instancian los servicios inyectando repositorios simulados (Mocks). Se evalúa que, dada una entrada específica, el servicio llame a los métodos correctos de la base de datos, maneje las excepciones esperadas (ej. `EntityNotFoundException`) y devuelva la estructura de datos correcta.
2. **Motores de Decisión e Inteligencia (Strategy Pattern):**
   - El `MotorSimilitud` y sus estrategias (`RazaStrategy`, `EdadStrategy`, `ChipStrategy`, etc.) del microservicio `ms-coincidencias`.
   - **Cómo se prueban:** Se realizan pruebas unitarias matemáticas puras. Se envían pares de mascotas (una base y una candidata) con distintas variaciones de datos para comprobar que los puntajes híbridos (positivos) y las penalizaciones (negativas) se calculen con una precisión decimal exacta sin superar el 100% ni bajar del 0%.
3. **Capa de Exposición (Controladores):**
   - **Cómo se prueban:** Usando `MockMvc` se simulan peticiones HTTP (GET, POST, PUT, DELETE) verificando los códigos de estado devueltos (200 OK, 400 Bad Request, 404 Not Found) y la estructura de los JSON.

### ¿Qué se excluye intencionalmente?
Para evitar "falsos positivos" de cobertura de código "bobo" (Dumb Code), se excluyen:
- **DTOs y Entidades (Models):** Dado que usan anotaciones de Lombok (`@Data`, `@Getter`, `@Setter`), testear un método "get" no previene bugs reales.
- **Configuraciones de Seguridad e Infraestructura pura.**

## 3. Métricas Actuales
El sistema supera de forma estable la métrica del **>75%** de cobertura en su capa lógica (Business Layer) a lo largo de los microservicios.

## 4. Cómo ejecutar las pruebas
Para compilar el proyecto saltando validaciones de checkstyle pero corriendo todos los tests unitarios con generación de reportes JaCoCo:
```bash
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent test org.jacoco:jacoco-maven-plugin:report
```
Los reportes en HTML se pueden visualizar abriendo `target/site/jacoco/index.html` de cada microservicio en el navegador.
