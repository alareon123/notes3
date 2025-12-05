package com.example.notes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO для входа пользователя (логина).
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * Для входа в Notes API нужно отправить email и пароль.
 * В ответ сервер вернёт токен авторизации, который нужно использовать
 * в заголовке X-AUTH-TOKEN для всех последующих запросов.
 *
 * ПРИМЕР JSON, КОТОРЫЙ ОТПРАВЛЯЕТСЯ:
 * {
 *   "email": "test@example.com",
 *   "password": "SecurePassword123"
 * }
 */
public class UserLoginRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    /**
     * Конструктор по умолчанию (требуется для Jackson)
     */
    public UserLoginRequest() {
    }

    /**
     * Конструктор с параметрами для удобного создания объекта
     *
     * @param email    Email пользователя
     * @param password Пароль пользователя
     */
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
