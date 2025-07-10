# Stage 1: Build
FROM gradle:8.3-jdk17 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean bootJar

# Stage 2: Run
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
