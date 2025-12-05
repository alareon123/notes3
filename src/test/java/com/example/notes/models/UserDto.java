package com.example.notes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO для данных пользователя.
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * API возвращает данные пользователя при регистрации, логине и получении профиля.
 * Этот класс позволяет десериализовать JSON-ответ в Java-объект.
 *
 * ПРИМЕР JSON ИЗ ОТВЕТА API:
 * {
 *   "id": "507f1f77bcf86cd799439011",
 *   "name": "Test User",
 *   "email": "test@example.com"
 * }
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) - игнорируем неизвестные поля в JSON,
 * чтобы не падать, если API добавит новые поля.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    /**
     * Конструктор по умолчанию (требуется для Jackson)
     */
    public UserDto() {
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
}
