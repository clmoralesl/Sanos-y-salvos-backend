# Etapa 1: Compilación
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos primero el pom.xml y descargamos dependencias para aprovechar caché
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copiamos código fuente y empaquetamos
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR generado desde la etapa de build
COPY --from=build /app/target/ms-coincidencias-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto del ms-coincidencias
EXPOSE 8083
# Comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
