package com.example.notes.models;

// Импортируем Jackson аннотацию для маппинга JSON полей
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO для создания новой заметки
 *
 * ЧТО ТАКОЕ REQUEST DTO:
 * - Это объект, который мы ОТПРАВЛЯЕМ на сервер (Java -> JSON)
 * - Содержит только те данные, которые пользователь должен предоставить при создании заметки
 * - НЕ содержит системные поля (id, created_at, updated_at) - их создает сервер
 *
 * РАЗНИЦА МЕЖДУ NoteDto И NoteCreateRequest:
 * - NoteDto - ПОЛНЫЙ объект заметки, который мы ПОЛУЧАЕМ от сервера (включает id, даты)
 * - NoteCreateRequest - ЧАСТИЧНЫЙ объект, который мы ОТПРАВЛЯЕМ серверу для создания заметки
 *
 * КАК РАБОТАЕТ МАППИНГ JAVA -> JSON:
 * 1. Создаем объект: new NoteCreateRequest("Купить молоко", "В магазине", "покупки")
 * 2. Jackson библиотека вызывает геттеры (getTitle, getDescription, getCategory)
 * 3. Jackson создает JSON: {"title": "Купить молоко", "description": "В магазине", "category": "покупки"}
 * 4. JSON отправляется на сервер через HTTP POST запрос
 *
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ В ТЕСТЕ:
 * NoteCreateRequest request = new NoteCreateRequest("Купить молоко", "В магазине", "покупки");
 * Response response = given()
 *     .contentType("application/json")
 *     .body(request)  // Jackson автоматически превратит в JSON
 *     .when()
 *     .post("/api/notes");
 */
public class NoteCreateRequest {

    // Заголовок новой заметки (обязательное поле)
    // JSON поле "title" -> Java поле title
    @JsonProperty("title")
    private String title;

    // Подробное описание новой заметки (необязательное поле)
    // JSON поле "description" -> Java поле description
    @JsonProperty("description")
    private String description;

    // Категория новой заметки (необязательное поле)
    // JSON поле "category" -> Java поле category
    @JsonProperty("category")
    private String category;

    /**
     * Конструктор по умолчанию (без параметров)
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Jackson требует конструктор без параметров для десериализации
     * - Также полезен, когда создаем объект и заполняем поля через сеттеры:
     *   NoteCreateRequest req = new NoteCreateRequest();
     *   req.setTitle("Купить молоко");
     *   req.setDescription("В магазине");
     */
    public NoteCreateRequest() {
    }

    /**
     * Конструктор с параметрами для удобного создания объекта
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Позволяет создать объект в одну строку с нужными данными
     * - Удобен в тестах: new NoteCreateRequest("Заголовок", "Описание", "Категория")
     * - Не обязателен для Jackson, но делает код более читаемым
     *
     * @param title Заголовок заметки
     * @param description Описание заметки
     * @param category Категория заметки
     */
    public NoteCreateRequest(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====
    // Jackson использует геттеры для преобразования Java объекта в JSON (сериализация)
    // Jackson использует сеттеры для преобразования JSON в Java объект (десериализация)

    /**
     * Получить заголовок заметки
     * @return Заголовок заметки
     */
    public String getTitle() {
        return title;
    }

    /**
     * Установить заголовок заметки
     * @param title Новый заголовок
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получить описание заметки
     * @return Описание заметки
     */
    public String getDescription() {
        return description;
    }

    /**
     * Установить описание заметки
     * @param description Новое описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получить категорию заметки
     * @return Категория заметки
     */
    public String getCategory() {
        return category;
    }

    /**
     * Установить категорию заметки
     * @param category Новая категория
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
