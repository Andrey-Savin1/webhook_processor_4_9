# Webhook Processor Service - Сервис для обработки входящих вебхуков от внешних провайдеров.

# Основные функции

- Приём вебхуков по HTTP POST
- Аутентификация провайдера через Basic Auth
- Маппинг данных из формата провайдера в общий DTO (ProcessDto)
- Сохранение информации о транзакции в БД
- Отправка данных в TransactionService с поддержкой повторных попыток
- Обработка ошибок и логирование

# Диаграммы:

Sequence Diagram, ER диаграмма, Component Diagram находятся в пакете [docs](docs) файл Diagrams.txt

# Технологический стек
- Язык: Java 21
- Фреймворки: Spring Boot 3.x, Spring Web MVC, Spring Data JPA, Spring Cloud OpenFeign, Spring Validation,
- Resilience4j 2.0+, Spring AOP 3.3.11
- База данных: PostgreSQL (+ Flyway для миграций)
- Формат данных: XML (вход), JSON (выход)
- Маппинг и сериализация : MapStruct 1.6.3, 
- Сериализация: Jackson Core 2.18.0 (JSON), Поддержка Java 8 Date/Time API через jackson-datatype-jsr310
- Тестирование: Testcontainers (PostgreSQL, MockServer), MockServer 5.15.0, REST Assured, JUnit 5, Spring Boot Test
- Генерация кода: Lombok 1.18.30
- Клиент для вызова внешних сервисов: Feign Client 4.0.0 (через spring-cloud-starter-openfeign)

# Запуск проекта

Создан файл docker-compose.yml и Dockerfile
Запустить контейнеры командой : docker-compose up --build
Остановить контейнеры командой : docker-compose down 
Сервис запускается на порту 8002, Postgres на порту 5432