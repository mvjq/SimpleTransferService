
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN ./gradlew dependencies --no-daemon
COPY src src/
RUN ./gradlew build --no-daemon || true
RUN ls -la /app/build/libs/

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="Marcos Vinicius Junqueira (mvjq)"
LABEL version="1.0.0"
LABEL description="Transfer Simple Service"

WORKDIR /app
RUN addgroup -S spring && \
    adduser -S spring -G spring && \
    chown -R spring:spring /app
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar
USER spring:spring

EXPOSE 8080

# JVM Options for containerized environment
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:InitialRAMPercentage=50.0 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/app/logs/heap-dump.hprof \
  -Djava.security.egd=file:/dev/./urandom"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS  -jar app.jar"]