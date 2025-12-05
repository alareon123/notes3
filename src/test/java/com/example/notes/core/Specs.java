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
 *
 * ДВЕ СПЕЦИФИКАЦИИ В ЭТОМ КЛАССЕ:
 * 1. requestSpec - базовая спецификация БЕЗ авторизации (для регистрации и логина)
 * 2. authSpec - спецификация С авторизацией (для работы с заметками)
 */
public class Specs {

    /**
     * Базовая спецификация запроса БЕЗ авторизации.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - Для регистрации пользователя (POST /users/register)
     * - Для входа (POST /users/login)
     * - Для любых публичных эндпоинтов, не требующих авторизации
     */
    public static RequestSpecification requestSpec;

    /**
     * Спецификация запроса С авторизацией (включает токен в заголовке X-AUTH-TOKEN).
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - Для работы с заметками (GET/POST/PUT/DELETE /notes)
     * - Для любых защищённых эндпоинтов, требующих авторизации
     *
     * ВАЖНО: Эта спецификация создаётся ПОСЛЕ успешного логина,
     * когда токен уже получен. До логина она будет null.
     */
    public static RequestSpecification authSpec;

    /**
     * Текущий токен авторизации.
     * Хранится для возможности удаления пользователя после теста.
     */
    private static String currentToken;

    /**
     * Создаёт и настраивает БАЗОВУЮ спецификацию запроса (без авторизации).
     * Этот метод вызывается один раз перед всеми тестами (из BaseApiTest.setUp()).
     *
     * ЧТО ДЕЛАЕТ МЕТОД:
     * Создаёт объект RequestSpecification с помощью паттерна "строитель" (Builder).
     */
    public static void setupRequestSpec() {
        // Создаём базовую спецификацию (без токена)
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(TestEnv.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Создаёт АВТОРИЗОВАННУЮ спецификацию с токеном.
     *
     * КОГДА ВЫЗЫВАТЬ:
     * После успешного логина, когда получен токен авторизации.
     *
     * ЧТО ДЕЛАЕТ:
     * Создаёт новую спецификацию, которая включает заголовок X-AUTH-TOKEN.
     * Все запросы с этой спецификацией будут автоматически авторизованы.
     *
     * ПРИМЕР ИСПОЛЬЗОВАНИЯ:
     * 1. String token = AuthClient.login(loginRequest).getToken();
     * 2. Specs.setupAuthSpec(token);
     * 3. NotesClient.createNote(request); // Автоматически использует authSpec
     *
     * @param token токен авторизации, полученный при логине
     */
    public static void setupAuthSpec(String token) {
        currentToken = token;
        authSpec = new RequestSpecBuilder()
                .setBaseUri(TestEnv.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                // Добавляем заголовок авторизации
                // X-AUTH-TOKEN - имя заголовка, которое требует Notes API
                .addHeader("X-AUTH-TOKEN", token)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Получить текущий токен авторизации.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - Для удаления аккаунта после теста (cleanup)
     * - Для проверки, что токен был установлен
     *
     * @return текущий токен или null, если пользователь не авторизован
     */
    public static String getCurrentToken() {
        return currentToken;
    }

    /**
     * Очистить авторизацию (сбросить токен и authSpec).
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - В tearDown() после удаления пользователя
     * - Для подготовки к новому тесту с новым пользователем
     */
    public static void clearAuth() {
        currentToken = null;
        authSpec = null;
    }
}
