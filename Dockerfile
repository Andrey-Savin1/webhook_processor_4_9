# Stage 1: Сборка проекта
FROM gradle:8.12.1-jdk21 AS builder
WORKDIR /webhook_processor_4_9
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Запуск приложения с минимальным JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /webhook_processor
# Копируем jar-файл из первого этапа
COPY --from=builder /webhook_processor_4_9/build/libs/webhook_processor-*.jar /webhook_processor.jar
# Запуск приложения
ENTRYPOINT ["java", "-jar", "/webhook_processor.jar"]