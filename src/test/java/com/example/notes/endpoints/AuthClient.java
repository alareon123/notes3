package com.example.notes.endpoints;

import com.example.notes.core.Specs;
import com.example.notes.models.AuthResponse;
import com.example.notes.models.UserLoginRequest;
import com.example.notes.models.UserRegisterRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * API-клиент для работы с авторизацией (Users API).
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * Notes API требует авторизации для работы с заметками.
 * Этот класс инкапсулирует все операции с пользователями:
 * - Регистрация нового пользователя
 * - Вход (получение токена)
 * - Удаление аккаунта
 *
 * FLOW АВТОРИЗАЦИИ:
 * 1. register() - создаём нового пользователя
 * 2. login() - получаем токен авторизации
 * 3. Используем токен в заголовке X-AUTH-TOKEN для всех запросов к Notes API
 * 4. deleteAccount() - удаляем пользователя после теста (cleanup)
 *
 * ПАТТЕРН DUAL-METHOD:
 * Как и в NotesClient, здесь используется паттерн с двумя методами:
 * - *Response() - возвращает полный Response для проверки статус-кодов и ошибок
 * - без Response - возвращает готовый объект для happy path
 */
public class AuthClient {

    // ==================== РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ ====================

    /**
     * Регистрирует нового пользователя и возвращает ПОЛНЫЙ HTTP-ответ.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - Для проверки ошибок (например, email уже занят)
     * - Для проверки статус-кода и структуры ответа
     *
     * HTTP ЗАПРОС:
     * POST /users/register
     * Content-Type: application/json
     * {
     *   "name": "Test User",
     *   "email": "test@example.com",
     *   "password": "Password123"
     * }
     *
     * @param request данные для регистрации (name, email, password)
     * @return полный HTTP-ответ
     */
    public static Response registerResponse(UserRegisterRequest request) {
        return given()
                .spec(Specs.requestSpec)
                .body(request)
                .when()
                .post(Endpoints.USERS_REGISTER);
    }

    /**
     * Регистрирует нового пользователя и возвращает данные созданного пользователя.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - В setUp() перед тестами для создания тестового пользователя
     * - Когда регистрация должна пройти успешно (happy path)
     *
     * @param request данные для регистрации
     * @return AuthResponse с данными пользователя (без токена - токен только при логине)
     */
    public static AuthResponse register(UserRegisterRequest request) {
        return registerResponse(request)
                .then()
                .statusCode(201) // 201 Created - пользователь успешно создан
                .extract()
                .jsonPath()
                .getObject("data", AuthResponse.class);
    }

    // ==================== ВХОД (ЛОГИН) ====================

    /**
     * Выполняет вход пользователя и возвращает ПОЛНЫЙ HTTP-ответ.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - Для проверки ошибок (неверный пароль, пользователь не найден)
     * - Для проверки статус-кода
     *
     * HTTP ЗАПРОС:
     * POST /users/login
     * Content-Type: application/json
     * {
     *   "email": "test@example.com",
     *   "password": "Password123"
     * }
     *
     * @param request данные для входа (email, password)
     * @return полный HTTP-ответ
     */
    public static Response loginResponse(UserLoginRequest request) {
        return given()
                .spec(Specs.requestSpec)
                .body(request)
                .when()
                .post(Endpoints.USERS_LOGIN);
    }

    /**
     * Выполняет вход и возвращает данные с токеном авторизации.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - В setUp() для получения токена перед тестами
     * - Когда вход должен пройти успешно (happy path)
     *
     * ВАЖНО: Полученный токен нужно сохранить и использовать
     * в заголовке X-AUTH-TOKEN для всех запросов к Notes API.
     *
     * @param request данные для входа
     * @return AuthResponse с токеном и данными пользователя
     */
    public static AuthResponse login(UserLoginRequest request) {
        return loginResponse(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", AuthResponse.class);
    }

    // ==================== УДАЛЕНИЕ АККАУНТА ====================

    /**
     * Удаляет аккаунт текущего авторизованного пользователя.
     * Возвращает ПОЛНЫЙ HTTP-ответ.
     *
     * ВАЖНО: Требует авторизации! Нужно передать токен в заголовке X-AUTH-TOKEN.
     *
     * HTTP ЗАПРОС:
     * DELETE /users/delete-account
     * X-AUTH-TOKEN: <token>
     *
     * @param token токен авторизации пользователя
     * @return полный HTTP-ответ
     */
    public static Response deleteAccountResponse(String token) {
        return given()
                .spec(Specs.requestSpec)
                .header("X-AUTH-TOKEN", token)
                .when()
                .delete(Endpoints.USERS_DELETE);
    }

    /**
     * Удаляет аккаунт текущего авторизованного пользователя.
     *
     * КОГДА ИСПОЛЬЗОВАТЬ:
     * - В tearDown() после тестов для очистки тестовых данных
     * - Для изоляции тестов (каждый тест работает со своим пользователем)
     *
     * @param token токен авторизации пользователя
     */
    public static void deleteAccount(String token) {
        deleteAccountResponse(token)
                .then()
                .statusCode(200);
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Полный цикл: регистрация + логин.
     * Возвращает токен для использования в тестах.
     *
     * УДОБНЫЙ МЕТОД для быстрого создания авторизованного пользователя:
     * String token = AuthClient.registerAndLogin(request);
     * // Теперь можно использовать token для работы с Notes API
     *
     * @param request данные для регистрации (name, email, password)
     * @return токен авторизации
     */
    public static String registerAndLogin(UserRegisterRequest request) {
        // 1. Регистрируем пользователя
        register(request);

        // 2. Логинимся с теми же данными и получаем токен
        UserLoginRequest loginRequest = new UserLoginRequest(
                request.getEmail(),
                request.getPassword()
        );
        AuthResponse authResponse = login(loginRequest);

        // 3. Возвращаем токен
        return authResponse.getToken();
    }
}
