package com.example.notes.models;

// Импортируем Jackson аннотации для работы с JSON
// Jackson - это библиотека, которая автоматически преобразует JSON в Java объекты и обратно
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) - Объект для передачи данных заметки
 *
 * ЧТО ТАКОЕ DTO:
 * - DTO это простой класс, который хранит данные и передает их между слоями приложения
 * - Используется для получения данных от API (когда сервер возвращает JSON с заметкой)
 * - НЕ содержит бизнес-логики, только данные и геттеры/сеттеры
 *
 * КАК РАБОТАЕТ МАППИНГ JSON -> JAVA:
 * 1. Сервер отправляет JSON: {"id": "123", "title": "Купить молоко", "completed": false}
 * 2. Jackson библиотека читает этот JSON
 * 3. Jackson создает объект NoteDto и вызывает сеттеры (setId, setTitle, setCompleted)
 * 4. В результате у нас есть Java объект с данными из JSON
 *
 * ПРИМЕР JSON, который превращается в этот объект:
 * {
 *   "id": "abc123",
 *   "title": "Купить продукты",
 *   "description": "Молоко, хлеб, яйца",
 *   "category": "покупки",
 *   "completed": false,
 *   "created_at": "2025-01-15T10:30:00",
 *   "updated_at": "2025-01-15T15:45:00"
 * }
 */
// Эта аннотация говорит Jackson: "Если в JSON есть неизвестные поля - просто игнорируй их"
// Например, если сервер добавит новое поле "priority", старый код не сломается
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteDto {

    // Уникальный идентификатор заметки (генерируется сервером)
    // @JsonProperty говорит: "В JSON это поле называется 'id', маппируй его в это Java поле"
    @JsonProperty("id")
    private String id;

    // Заголовок заметки (например: "Купить продукты")
    // JSON поле "title" -> Java поле title
    @JsonProperty("title")
    private String title;

    // Подробное описание заметки (например: "Молоко, хлеб, яйца")
    // JSON поле "description" -> Java поле description
    @JsonProperty("description")
    private String description;

    // Категория заметки (например: "работа", "покупки", "личное")
    // JSON поле "category" -> Java поле category
    @JsonProperty("category")
    private String category;

    // Статус выполнения заметки (true = выполнена, false = не выполнена)
    // Используем Boolean (объект), а не boolean (примитив), чтобы поле могло быть null
    // JSON поле "completed" -> Java поле completed
    @JsonProperty("completed")
    private Boolean completed;

    // Дата и время создания заметки
    // ВАЖНО: в JSON используется snake_case ("created_at"), в Java - camelCase (createdAt)
    // @JsonProperty("created_at") делает правильный маппинг между этими стилями
    @JsonProperty("created_at")
    private String createdAt;

    // Дата и время последнего обновления заметки
    // JSON поле "updated_at" -> Java поле updatedAt
    @JsonProperty("updated_at")
    private String updatedAt;

    /**
     * Конструктор по умолчанию (без параметров)
     *
     * ЗАЧЕМ ОН НУЖЕН:
     * - Jackson использует этот конструктор для создания пустого объекта
     * - Затем Jackson вызывает сеттеры (setId, setTitle и т.д.) для заполнения полей
     * - Без этого конструктора Jackson не сможет создать объект из JSON
     */
    public NoteDto() {
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====
    // Геттеры и сеттеры - это методы для получения и изменения приватных полей
    //
    // ЗАЧЕМ ОНИ НУЖНЫ:
    // 1. Инкапсуляция - поля private, доступ только через методы
    // 2. Jackson использует их для чтения и записи значений
    // 3. Возможность добавить логику в будущем (например, валидацию)
    //
    // NAMING CONVENTION (правила именования):
    // - Геттер: get + Название поля с большой буквы (getId, getTitle)
    // - Сеттер: set + Название поля с большой буквы (setId, setTitle)

    /**
     * Получить ID заметки
     * @return ID заметки (строка, например "abc123")
     */
    public String getId() {
        return id;
    }

    /**
     * Установить ID заметки
     * @param id ID заметки (обычно устанавливается Jackson при чтении JSON)
     */
    public void setId(String id) {
        this.id = id;  // this.id - поле класса, id - параметр метода
    }

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

    /**
     * Проверить, выполнена ли заметка
     * @return true если выполнена, false если нет, null если не установлено
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Установить статус выполнения заметки
     * @param completed true для выполненной, false для невыполненной
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    /**
     * Получить дату создания заметки
     * @return Дата создания в виде строки (например "2025-01-15T10:30:00")
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Установить дату создания заметки
     * @param createdAt Дата создания
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Получить дату последнего обновления заметки
     * @return Дата обновления в виде строки
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Установить дату последнего обновления заметки
     * @param updatedAt Дата обновления
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
