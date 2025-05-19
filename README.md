# 📦 PriceMonitor Application

## 📄 Спецификация приложения

OpenAPI-спецификация доступна в файле [`openapi.json`](./openapi.json)

---

## 🚀 Установка и запуск

### 📋 Требования

Для установки и запуска приложения необходимы следующие компоненты:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 21](https://openjdk.org/projects/jdk/21/)
- PostgreSQL (если используется внешняя база данных)

---

### 🔐 Обязательные переменные окружения

Убедитесь, что заданы следующие переменные окружения для корректной работы приложения:

| Переменная                 | Описание                               | Пример значения                    |
|----------------------------|----------------------------------------|------------------------------------|
| `SPRING_DATASOURCE_URL`   | JDBC URL базы данных                   | `jdbc:postgresql://localhost:5432/pricemonitor` |
| `SPRING_DATASOURCE_USERNAME` | Имя пользователя БД                | `postgres`                         |
| `SPRING_DATASOURCE_PASSWORD` | Пароль к БД                        | `postgres`                         |
| `JWT_SECRET`              | Секрет для подписи JWT токенов         | `0301d0977d6ca3e10483caf8df7aa096...` |
| `JWT_EXPIRATION`          | Время жизни JWT в миллисекундах        | `3600000`                          |

---

### 🛠️ Сборка проекта и Docker-образа

* Скопируйте архив с приложением на машину, где будет производиться запуск.

* Соберите проект с помощью Gradle:
   ```bash
   ./gradlew bootJar

### 🧪 Запуск приложения

#### ✅ Если база данных уже существует

* Отредактируйте файл docker-compose.yml, указав актуальные параметры подключения к БД в секции environment.
* Запустите приложение:
    ```bash 
    docker-compose up

#### 🆕 Если собственной БД нет

* Используйте файл docker-compose-with-postgres.yml, который поднимет приложение и PostgreSQL одновременно:
    ```bash
    docker-compose -f docker-compose-with-postgres.yml up
