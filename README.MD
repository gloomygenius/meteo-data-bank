# Требования для окружения

JDK 1.8, PostgresDB
Для запуска тестов понадобится Docker
# Запуск
- Предварительно установить PostgresDB и прописать настройки базы в src/main/resources/application.properties
- Прописать в консоли ./gradlew bootRun (из корня проекта). Если установлен gradle то можно запустить через gradle bootRun
- Зайти на localhost:8080/swagger-ui.html и начать пользоваться API (по дефолту user/123456)

# Собрать приложение в исполняемый jar-файл:
cmd: ./gradlew jar
