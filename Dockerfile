# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy gradle wrapper and dependency config first for layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon || true

# Copy source and build
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
