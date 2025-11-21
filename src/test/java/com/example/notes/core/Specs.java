package com.example.notes.core;

import com.example.notes.config.TestEnv;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Класс для хранения спецификаций Rest-Assured.
 *
 * ЧТО ТАКОЕ СПЕЦИФИКАЦИЯ (RequestSpecification):
 * Это шаблон настроек для HTTP-запросов. Вместо того, чтобы в каждом тесте писать:
 *
 * given()
 *     .baseUri("http://localhost:8080")
 *     .contentType(JSON)
 *     .accept(JSON)
 *     .log().all()
 * .when()
 *     .get("/api/notes")
 *
 * Мы один раз создаём спецификацию и потом просто пишем:
 *
 * given()
 *     .spec(requestSpec)  // Все настройки уже внутри
 * .when()
 *     .get("/api/notes")
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * 1. Централизованное хранение конфигурации для всех API-запросов
 * 2. Избавляет от дублирования кода в тестах
 * 3. Упрощает изменение настроек (меняем в одном месте)
 *
 * КАК РАБОТАЕТ Rest-Assured:
 * Rest-Assured - это библиотека для тестирования REST API на Java.
 * Она позволяет делать HTTP-запросы (GET, POST, PUT, DELETE) и проверять ответы.
 */
public class Specs {

    /**
     * Спецификация запроса для всех API-тестов.
     *
     * ПОЧЕМУ STATIC:
     * - static означает, что переменная принадлежит классу, а не объекту
     * - Можно обращаться через Specs.requestSpec без создания объекта
     * - Все тесты будут использовать одну и ту же спецификацию
     *
     * ПОЧЕМУ PUBLIC:
     * - Нужно, чтобы тесты могли получить доступ к этой спецификации
     * - Используется как: given().spec(Specs.requestSpec)
     */
    public static RequestSpecification requestSpec;

    /**
     * Создаёт и настраивает спецификацию запроса для Rest-Assured.
     * Этот метод вызывается один раз перед всеми тестами (из BaseApiTest.setUp()).
     *
     * ПОЧЕМУ STATIC:
     * - Метод вызывается из static метода BaseApiTest.setUp()
     * - Не требует создания объекта класса Specs
     *
     * ЧТО ДЕЛАЕТ МЕТОД:
     * Создаёт объект RequestSpecification с помощью паттерна "строитель" (Builder).
     * Паттерн Builder позволяет пошагово настраивать сложный объект.
     */
    public static void setupRequestSpec() {
        // Создаём спецификацию с помощью RequestSpecBuilder
        requestSpec = new RequestSpecBuilder()
                // Устанавливаем базовый URL (например, http://localhost:8080)
                // Берётся из переменных окружения или конфигурации
                // После этого в тестах можно писать только .get("/api/notes"),
                // а полный URL будет http://localhost:8080/api/notes
                .setBaseUri(TestEnv.getBaseUrl())

                // Устанавливаем тип содержимого запроса как JSON
                // Добавляет заголовок: Content-Type: application/json
                // Это говорит серверу, что мы отправляем данные в формате JSON
                .setContentType(ContentType.JSON)

                // Говорим серверу, что ожидаем ответ в формате JSON
                // Добавляет заголовок: Accept: application/json
                // Это говорит серверу, что хотим получить ответ в JSON
                .setAccept(ContentType.JSON)

                // Включаем подробное логирование всех запросов
                // В консоли будут видны: URL, заголовки, тело запроса
                // Очень полезно для отладки тестов
                // LogDetail.ALL - логировать всё (можно выбрать только headers или только body)
                .log(LogDetail.ALL)

                // Завершаем создание и возвращаем готовую спецификацию
                .build();
    }
}
