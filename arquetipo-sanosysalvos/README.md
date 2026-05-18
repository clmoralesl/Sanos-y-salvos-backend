# Arquetipo Base - Sanos y Salvos

Este directorio contiene la estructura base para la creación de nuevos microservicios dentro del ecosistema de Sanos y Salvos.

## Requisitos Base Incluidos
- Java 17 (LTS).
- Spring Boot 3.2.4.
- Lombok para reducción de código boilerplate.
- Spring Data JPA para persistencia.
- Validation para validación de DTOs.

## Cómo usar este arquetipo
1. Copia el contenido de esta carpeta en un nuevo directorio.
2. Actualiza el artifactId y name en el archivo pom.xml.
3. Ejecuta mvn clean install para verificar las dependencias.
4. Define el archivo application.yml en src/main/resources.

## Dockerización
Cada microservicio generado a partir de este arquetipo debe incluir un Dockerfile multietapa (build & run) basado en eclipse-temurin:17-jre-alpine para garantizar imágenes ligeras y seguras.
