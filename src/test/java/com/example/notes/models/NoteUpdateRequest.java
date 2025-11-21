package com.example.notes.models;

// Импортируем Jackson аннотацию для маппинга JSON полей
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO для обновления существующей заметки
 *
 * ЧТО ТАКОЕ UPDATE REQUEST:
 * - Это объект, который мы ОТПРАВЛЯЕМ на сервер для изменения существующей заметки
 * - Содержит поля, которые пользователь может изменить
 * - НЕ содержит id (он передается в URL, например PUT /api/notes/123)
 * - НЕ содержит created_at, updated_at (их обновляет сервер автоматически)
 *
 * РАЗНИЦА МЕЖДУ CREATE И UPDATE:
 * - NoteCreateRequest - для создания новой заметки (POST /api/notes)
 *   НЕ содержит поле "completed" (новая заметка всегда не выполнена)
 * - NoteUpdateRequest - для изменения существующей заметки (PUT /api/notes/123)
 *   СОДЕРЖИТ поле "completed" (можно пометить заметку как выполненную)
 *
 * КАК РАБОТАЕТ ОБНОВЛЕНИЕ:
 * 1. Создаем объект: new NoteUpdateRequest("Новый заголовок", "Новое описание", "работа", true)
 * 2. Jackson преобразует в JSON: {"title": "Новый заголовок", "description": "Новое описание", "category": "работа", "completed": true}
 * 3. Отправляем PUT запрос: PUT /api/notes/123 с этим JSON в теле
 * 4. Сервер обновляет заметку с ID=123
 *
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ В ТЕСТЕ:
 * // Создаем заметку
 * String noteId = createNote("Старый заголовок", "Старое описание", "покупки");
 *
 * // Обновляем заметку
 * NoteUpdateRequest updateRequest = new NoteUpdateRequest("Новый заголовок", "Новое описание", "работа", true);
 * given()
 *     .contentType("application/json")
 *     .body(updateRequest)  // Jackson превратит в JSON
 *     .when()
 *     .put("/api/notes/" + noteId)
 *     .then()
 *     .statusCode(200);
 */
public class NoteUpdateRequest {

    // Новый заголовок заметки (необязательное поле - можно не менять)
    // JSON поле "title" -> Java поле title
    @JsonProperty("title")
    private String title;

    // Новое описание заметки (необязательное поле)
    // JSON поле "description" -> Java поле description
    @JsonProperty("description")
    private String description;

    // Новая категория заметки (необязательное поле)
    // JSON поле "category" -> Java поле category
    @JsonProperty("category")
    private String category;

    // Статус выполнения заметки (необязательное поле)
    // ВАЖНО: это поле есть только в Update, но нет в Create
    // При создании заметка всегда не выполнена (completed=false)
    // При обновлении можно изменить статус на выполнена (completed=true)
    // JSON поле "completed" -> Java поле completed
    @JsonProperty("completed")
    private Boolean completed;

    /**
     * Конструктор по умолчанию (без параметров)
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Требуется для Jackson десериализации
     * - Позволяет создать пустой объект и заполнить только нужные поля:
     *   NoteUpdateRequest req = new NoteUpdateRequest();
     *   req.setCompleted(true);  // Обновляем только статус
     */
    public NoteUpdateRequest() {
    }

    /**
     * Конструктор с параметрами для создания полного объекта обновления
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Удобно создавать объект в одну строку
     * - Полезен в тестах, когда нужно обновить все поля сразу
     *
     * @param title Новый заголовок заметки
     * @param description Новое описание заметки
     * @param category Новая категория заметки
     * @param completed Новый статус выполнения (true = выполнена, false = не выполнена)
     */
    public NoteUpdateRequest(String title, String description, String category, Boolean completed) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.completed = completed;
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====
    // Jackson использует геттеры для сериализации (Java -> JSON)
    // Jackson использует сеттеры для десериализации (JSON -> Java)

    /**
     * Получить новый заголовок заметки
     * @return Новый заголовок или null, если не обновляется
     */
    public String getTitle() {
        return title;
    }

    /**
     * Установить новый заголовок заметки
     * @param title Новый заголовок
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получить новое описание заметки
     * @return Новое описание или null, если не обновляется
     */
    public String getDescription() {
        return description;
    }

    /**
     * Установить новое описание заметки
     * @param description Новое описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получить новую категорию заметки
     * @return Новая категория или null, если не обновляется
     */
    public String getCategory() {
        return category;
    }

    /**
     * Установить новую категорию заметки
     * @param category Новая категория
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Получить новый статус выполнения заметки
     * @return true если выполнена, false если нет, null если не обновляется
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Установить новый статус выполнения заметки
     * @param completed true для выполненной, false для невыполненной
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
