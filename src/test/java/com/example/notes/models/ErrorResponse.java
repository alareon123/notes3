package com.example.notes.models;

// Импортируем Jackson аннотации для работы с JSON ошибками
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для представления ошибки от API
 *
 * ЧТО ТАКОЕ ERROR RESPONSE:
 * - Это объект, который мы ПОЛУЧАЕМ от сервера, когда происходит ошибка
 * - Сервер возвращает его вместо успешного ответа при ошибках (400, 404, 500 и т.д.)
 * - Содержит информацию о том, что пошло не так
 *
 * КОГДА ИСПОЛЬЗУЕТСЯ:
 * - 400 Bad Request - неправильные данные (пустой заголовок, некорректная категория)
 * - 404 Not Found - заметка не найдена
 * - 500 Internal Server Error - ошибка на сервере
 *
 * КАК РАБОТАЕТ В ТЕСТАХ:
 * 1. Отправляем неправильный запрос (например, создаем заметку без заголовка)
 * 2. Сервер возвращает HTTP 400 и JSON с ошибкой
 * 3. Jackson преобразует JSON в объект ErrorResponse
 * 4. Проверяем, что сообщение об ошибке правильное
 *
 * ПРИМЕР JSON ОШИБКИ от сервера:
 * {
 *   "message": "Заголовок не может быть пустым",
 *   "code": 400,
 *   "status": 400
 * }
 *
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ В ТЕСТЕ:
 * ErrorResponse error = given()
 *     .contentType("application/json")
 *     .body(new NoteCreateRequest("", "Описание", "покупки"))  // Пустой заголовок - ошибка!
 *     .when()
 *     .post("/api/notes")
 *     .then()
 *     .statusCode(400)  // Ожидаем код ошибки
 *     .extract()
 *     .as(ErrorResponse.class);  // Преобразуем JSON ответ в ErrorResponse
 *
 * // Проверяем сообщение об ошибке
 * assertEquals("Заголовок не может быть пустым", error.getMessage());
 */
// Если в JSON ошибки есть дополнительные поля (например, "timestamp", "path") - игнорируем их
// Нам нужны только message, code и status
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    // Человекочитаемое сообщение об ошибке
    // Примеры: "Заметка не найдена", "Заголовок не может быть пустым", "Внутренняя ошибка сервера"
    // JSON поле "message" -> Java поле message
    @JsonProperty("message")
    private String message;

    // Код ошибки (обычно совпадает со статусом HTTP)
    // Примеры: 400 (Bad Request), 404 (Not Found), 500 (Internal Server Error)
    // JSON поле "code" -> Java поле code
    @JsonProperty("code")
    private Integer code;

    // HTTP статус ошибки (обычно совпадает с code)
    // Примеры: 400, 404, 500
    // JSON поле "status" -> Java поле status
    // ПРИМЕЧАНИЕ: В некоторых API есть и code, и status. Иногда они различаются
    // (status = HTTP статус, code = внутренний код ошибки приложения)
    @JsonProperty("status")
    private Integer status;

    /**
     * Конструктор по умолчанию (без параметров)
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Jackson использует его для создания объекта из JSON ошибки
     * - Сервер отправляет JSON -> Jackson создает ErrorResponse -> вызывает сеттеры
     */
    public ErrorResponse() {
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====
    // Jackson использует сеттеры для заполнения полей из JSON
    // Геттеры позволяют нам в тестах получить информацию об ошибке

    /**
     * Получить сообщение об ошибке
     * @return Текст сообщения об ошибке (например, "Заметка не найдена")
     */
    public String getMessage() {
        return message;
    }

    /**
     * Установить сообщение об ошибке
     * @param message Текст ошибки (устанавливается Jackson при чтении JSON)
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Получить код ошибки
     * @return Числовой код ошибки (например, 400, 404, 500)
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Установить код ошибки
     * @param code Код ошибки (устанавливается Jackson при чтении JSON)
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Получить HTTP статус ошибки
     * @return HTTP статус код (например, 400, 404, 500)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Установить HTTP статус ошибки
     * @param status HTTP статус (устанавливается Jackson при чтении JSON)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
