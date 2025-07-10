# Stage 1: Build
FROM gradle:8.3-jdk17 AS builder
COPY . /app           # 호스트의 전체 소스 → 컨테이너의 /app에 복사
WORKDIR /app          # 이후 모든 명령은 /app 기준으로 실행
RUN ./gradlew bootJar # /app 안에서 Gradle 빌드 실행됨

# Stage 2: Run
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
