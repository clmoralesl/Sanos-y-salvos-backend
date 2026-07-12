FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

RUN apk add --no-cache openssl \
    && openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
       -keyout /tmp/server.key -out /tmp/server.crt -subj "/CN=api-gateway.local" \
    && openssl pkcs12 -export -in /tmp/server.crt -inkey /tmp/server.key \
       -out /app/keystore.p12 -name springboot -password pass:secret

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8443", "--server.ssl.key-store=/app/keystore.p12", "--server.ssl.key-store-password=secret", "--server.ssl.key-store-type=PKCS12", "--server.ssl.enabled=true"]