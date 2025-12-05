package com.example.notes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO для ответа авторизации (логина).
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * При успешном логине API возвращает токен авторизации и данные пользователя.
 * Токен нужно использовать в заголовке X-AUTH-TOKEN для всех последующих запросов.
 *
 * СТРУКТУРА ОТВЕТА API:
 * {
 *   "success": true,
 *   "status": 200,
 *   "message": "Login successful",
 *   "data": {
 *     "id": "507f1f77bcf86cd799439011",
 *     "name": "Test User",
 *     "email": "test@example.com",
 *     "token": "abc123xyz..."
 *   }
 * }
 *
 * ВАЖНО: Данные находятся внутри поля "data", поэтому при десериализации
 * нужно извлекать: .jsonPath().getObject("data", AuthResponse.class)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    /**
     * Токен авторизации.
     * Используется в заголовке X-AUTH-TOKEN для авторизованных запросов.
     */
    @JsonProperty("token")
    private String token;

    /**
     * Конструктор по умолчанию (требуется для Jackson)
     */
    public AuthResponse() {
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
