package com.example.notes.tests;

import com.example.notes.core.BaseApiTest;
import com.example.notes.data.TestData;
import com.example.notes.endpoints.NotesClient;
import com.example.notes.models.NoteCreateRequest;
import com.example.notes.models.NoteDto;
import com.example.notes.models.NoteUpdateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * CRUD-ТЕСТЫ для заметок
 *
 * Что такое CRUD?
 * CRUD - это акроним из первых букв четырёх основных операций с данными:
 * C - Create (Создать) - добавление новой записи в базу данных
 * R - Read (Прочитать) - получение существующей записи из базы данных
 * U - Update (Обновить) - изменение существующей записи в базе данных
 * D - Delete (Удалить) - удаление записи из базы данных
 *
 * Цель CRUD-тестов:
 * - Проверить, что все основные операции с заметками работают корректно
 * - Убедиться, что данные сохраняются, изменяются и удаляются правильно
 * - Проверить, что API возвращает корректные данные после каждой операции
 *
 * Эти тесты проверяют "счастливый путь" (happy path) - когда всё идёт правильно
 * и пользователь передаёт корректные данные.
 */
@DisplayName("Notes CRUD Tests") // Название группы тестов для отчёта
public class NotesCrudTest extends BaseApiTest { // Наследуемся от базового класса

    /**
     * ТЕСТ: Полный цикл CRUD-операций
     *
     * Что проверяет этот тест:
     * Весь жизненный цикл заметки от создания до удаления:
     * 1. Создание новой заметки (Create)
     * 2. Получение созданной заметки по ID (Read)
     * 3. Обновление данных заметки (Update)
     * 4. Удаление заметки (Delete)
     * 5. Проверка, что заметка действительно удалена
     *
     * Это самый важный тест, проверяющий всю базовую функциональность.
     */
    @Test // JUnit запустит этот метод как тест
    @DisplayName("Full CRUD cycle: Create, Read, Update, Delete note") // Название для отчёта
    public void fullCrudCycle() {

        // ==================== ЭТАП 1: CREATE (Создание заметки) ====================

        // Получаем тестовые данные для создания заметки
        // TestData.simpleNote() возвращает объект с заголовком, описанием и категорией
        NoteCreateRequest createRequest = TestData.simpleNote();

        // Отправляем POST-запрос на сервер для создания заметки
        // Сервер создаёт заметку в базе данных и возвращает её с присвоенным ID
        NoteDto createdNote = NotesClient.createNote(createRequest);

        // ПРОВЕРКИ после создания заметки:

        // Проверка 1: ID заметки не должен быть null (сервер должен присвоить ID)
        // assertThat - метод из библиотеки Hamcrest для проверки условий
        // Первый параметр - сообщение об ошибке, если проверка провалится
        // Второй параметр - что проверяем (фактическое значение)
        // Третий параметр - matcher (ожидаемое условие)
        assertThat("Note ID should not be null", createdNote.getId(), notNullValue());

        // Проверка 2: Заголовок в ответе должен совпадать с отправленным
        // equalTo() - matcher, который проверяет точное совпадение значений
        assertThat("Title should match", createdNote.getTitle(), equalTo(createRequest.getTitle()));

        // Проверка 3: Описание в ответе должно совпадать с отправленным
        assertThat("Description should match", createdNote.getDescription(), equalTo(createRequest.getDescription()));

        // Проверка 4: Категория в ответе должна совпадать с отправленной
        assertThat("Category should match", createdNote.getCategory(), equalTo(createRequest.getCategory()));

        // Проверка 5: Timestamp создания должен быть заполнен
        // Сервер автоматически ставит дату и время создания
        assertThat("Created at timestamp should not be null", createdNote.getCreatedAt(), notNullValue());

        // Сохраняем ID созданной заметки - он понадобится для дальнейших операций
        String noteId = createdNote.getId();

        // ==================== ЭТАП 2: READ (Чтение заметки) ====================

        // Отправляем GET-запрос на сервер для получения заметки по ID
        // Проверяем, что сервер может вернуть ранее созданную заметку
        NoteDto fetchedNote = NotesClient.getNote(noteId);

        // ПРОВЕРКИ после получения заметки:

        // Проверка 1: ID полученной заметки совпадает с тем, который мы запрашивали
        assertThat("Fetched note ID should match", fetchedNote.getId(), equalTo(noteId));

        // Проверка 2: Заголовок не изменился после сохранения в базе
        assertThat("Fetched title should match", fetchedNote.getTitle(), equalTo(createRequest.getTitle()));

        // Проверка 3: Описание не изменилось после сохранения в базе
        assertThat("Fetched description should match", fetchedNote.getDescription(), equalTo(createRequest.getDescription()));

        // ==================== ЭТАП 3: UPDATE (Обновление заметки) ====================

        // Получаем новые данные для обновления заметки
        // TestData.updateNoteData() возвращает изменённые заголовок, описание, категорию
        NoteUpdateRequest updateRequest = TestData.updateNoteData();

        // Отправляем PUT-запрос на сервер для обновления заметки
        // Передаём ID заметки и новые данные
        NoteDto updatedNote = NotesClient.updateNote(noteId, updateRequest);

        // ПРОВЕРКИ после обновления заметки:

        // Проверка 1: ID заметки остался прежним (заметка не пересоздалась, а обновилась)
        assertThat("Updated note ID should remain the same", updatedNote.getId(), equalTo(noteId));

        // Проверка 2: Заголовок изменился на новый
        assertThat("Updated title should match", updatedNote.getTitle(), equalTo(updateRequest.getTitle()));

        // Проверка 3: Описание изменилось на новое
        assertThat("Updated description should match", updatedNote.getDescription(), equalTo(updateRequest.getDescription()));

        // Проверка 4: Категория изменилась на новую
        assertThat("Updated category should match", updatedNote.getCategory(), equalTo(updateRequest.getCategory()));

        // Проверка 5: Статус "завершено" изменился согласно запросу
        assertThat("Completed status should match", updatedNote.getCompleted(), equalTo(updateRequest.getCompleted()));

        // ==================== ЭТАП 4: DELETE (Удаление заметки) ====================

        // Отправляем DELETE-запрос на сервер для удаления заметки
        // После этого заметка должна быть удалена из базы данных
        NotesClient.deleteNote(noteId);

        // ==================== ЭТАП 5: ПРОВЕРКА УДАЛЕНИЯ ====================

        // Пытаемся получить удалённую заметку
        // Ожидаем, что сервер вернёт ошибку 404 (Not Found - не найдено)
        Response response = NotesClient.getNoteResponse(noteId);

        // Проверяем, что сервер вернул код 404
        // Это подтверждает, что заметка действительно удалена
        response.then()
                .statusCode(404); // 404 = Not Found (ресурс не найден)

        // Если все 5 этапов прошли успешно - тест пройден!
    }

    /**
     * ТЕСТ: Создание заметки и проверка всех полей ответа
     *
     * Что проверяет этот тест:
     * - Создание новой заметки работает корректно
     * - Сервер возвращает все необходимые поля в ответе
     * - ID генерируется и не пустой
     * - Все отправленные данные сохраняются без искажений
     * - Timestamp создания заполняется автоматически
     *
     * Это упрощённый тест, проверяющий только операцию Create (создание)
     * и правильность возвращаемых данных.
     */
    @Test // Помечаем метод как тест
    @DisplayName("Create note and verify response fields") // Название для отчёта
    public void createNoteAndVerifyFields() {

        // ШАГ 1: Подготавливаем данные для создания заметки
        NoteCreateRequest request = TestData.simpleNote();

        // ШАГ 2: Отправляем запрос на создание заметки
        NoteDto note = NotesClient.createNote(request);

        // ШАГ 3: ПРОВЕРКИ возвращённых данных

        // Проверка 1: ID не null (сервер присвоил ID)
        // notNullValue() - matcher, проверяющий что значение не null
        assertThat("Note should have non-null ID", note.getId(), notNullValue());

        // Проверка 2: ID не пустая строка (ID реально заполнен)
        // not() - инвертирует matcher (НЕ пустая строка)
        // emptyString() - matcher, проверяющий пустую строку ""
        assertThat("Note should have non-empty ID", note.getId(), not(emptyString()));

        // Проверка 3: Заголовок совпадает с отправленным
        assertThat("Title should match request", note.getTitle(), equalTo(request.getTitle()));

        // Проверка 4: Описание совпадает с отправленным
        assertThat("Description should match request", note.getDescription(), equalTo(request.getDescription()));

        // Проверка 5: Категория совпадает с отправленной
        assertThat("Category should match request", note.getCategory(), equalTo(request.getCategory()));

        // Проверка 6: Timestamp создания заполнен автоматически
        assertThat("Created at should not be null", note.getCreatedAt(), notNullValue());

        // ШАГ 4: Очистка (Cleanup)
        // Удаляем созданную заметку, чтобы не засорять базу данных тестовыми данными
        // Это важно, чтобы тесты не влияли друг на друга
        NotesClient.deleteNote(note.getId());

        // Если все проверки прошли и очистка выполнена - тест пройден!
    }
}
