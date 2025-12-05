package com.example.notes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO для регистрации нового пользователя.
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * При регистрации на Notes API нужно отправить данные пользователя:
 * - name (имя)
 * - email (email, используется для входа)
 * - password (пароль)
 *
 * ПРИМЕР JSON, КОТОРЫЙ ОТПРАВЛЯЕТСЯ:
 * {
 *   "name": "Test User",
 *   "email": "test@example.com",
 *   "password": "SecurePassword123"
 * }
 */
public class UserRegisterRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    /**
     * Конструктор по умолчанию (требуется для Jackson)
     */
    public UserRegisterRequest() {
    }

    /**
     * Конструктор с параметрами для удобного создания объекта
     *
     * @param name     Имя пользователя
     * @param email    Email (используется для входа)
     * @param password Пароль (минимум 6 символов)
     */
    public UserRegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
