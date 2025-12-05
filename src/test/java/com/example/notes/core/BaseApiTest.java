package com.example.notes.core;

import com.example.notes.data.TestData;
import com.example.notes.endpoints.AuthClient;
import com.example.notes.models.UserRegisterRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Базовый класс для всех API-тестов.
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * Все тестовые классы наследуются от BaseApiTest, чтобы не дублировать код настройки.
 * Вместо того, чтобы в каждом тестовом классе писать методы setUp/tearDown, мы пишем их один раз здесь.
 *
 * LIFECYCLE ТЕСТОВ (порядок выполнения):
 * 1. @BeforeAll setUp() - один раз перед всеми тестами (настройка базового spec)
 * 2. @BeforeEach setUpAuth() - перед КАЖДЫМ тестом (создание нового пользователя)
 * 3. @Test myTest() - выполнение теста
 * 4. @AfterEach tearDownAuth() - после КАЖДОГО теста (удаление пользователя)
 *
 * ИЗОЛЯЦИЯ ТЕСТОВ:
 * Каждый тест работает со своим пользователем:
 * - Перед тестом создаётся новый пользователь с уникальным email
 * - После теста пользователь удаляется
 * - Тесты не влияют друг на друга
 *
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ:
 * public class NotesApiTest extends BaseApiTest {
 *     @Test
 *     void myTest() {
 *         // Specs.authSpec уже содержит токен авторизации
 *         // Можно сразу работать с заметками
 *         NotesClient.createNote(request);
 *     }
 * }
 */
public class BaseApiTest {

    /**
     * Метод настройки, который выполняется ОДИН РАЗ перед всеми тестами в классе.
     *
     * АННОТАЦИЯ @BeforeAll:
     * - Выполняется один раз перед запуском всех тестов в классе
     * - Метод ДОЛЖЕН быть static (статическим)
     * - Используется для "тяжёлой" настройки, которую не нужно повторять перед каждым тестом
     *
     * ЧТО ПРОИСХОДИТ ВНУТРИ:
     * Создаётся базовая спецификация (без авторизации) для регистрации и логина.
     */
    @BeforeAll
    public static void setUp() {
        // Настраиваем базовую спецификацию запросов (без авторизации)
        Specs.setupRequestSpec();
    }

    /**
     * Метод, который выполняется ПЕРЕД КАЖДЫМ тестом.
     *
     * АННОТАЦИЯ @BeforeEach:
     * - Выполняется перед каждым @Test методом
     * - НЕ static (имеет доступ к полям экземпляра)
     *
     * ЧТО ПРОИСХОДИТ ВНУТРИ:
     * 1. Генерируется уникальный пользователь (с уникальным email)
     * 2. Пользователь регистрируется в системе
     * 3. Выполняется логин и получается токен
     * 4. Создаётся authSpec с токеном для авторизованных запросов
     *
     * ПОСЛЕ ВЫПОЛНЕНИЯ:
     * - Specs.authSpec содержит токен и готов к использованию
     * - Specs.getCurrentToken() возвращает токен (для cleanup)
     */
    @BeforeEach
    public void setUpAuth() {
        // 1. Генерируем уникальные данные для нового пользователя
        UserRegisterRequest userRequest = TestData.randomUser();

        // 2. Регистрируем пользователя и получаем токен
        String token = AuthClient.registerAndLogin(userRequest);

        // 3. Создаём авторизованную спецификацию с токеном
        Specs.setupAuthSpec(token);
    }

    /**
     * Метод, который выполняется ПОСЛЕ КАЖДОГО теста.
     *
     * АННОТАЦИЯ @AfterEach:
     * - Выполняется после каждого @Test метода
     * - Выполняется даже если тест упал (важно для cleanup!)
     *
     * ЧТО ПРОИСХОДИТ ВНУТРИ:
     * 1. Удаляется аккаунт текущего пользователя
     * 2. Очищается authSpec и токен
     *
     * ЗАЧЕМ УДАЛЯТЬ ПОЛЬЗОВАТЕЛЯ:
     * - Не засорять базу данных тестовыми данными
     * - Обеспечить изоляцию тестов
     * - Email должен быть уникальным, иначе следующая регистрация упадёт
     */
    @AfterEach
    public void tearDownAuth() {
        // Получаем токен текущего пользователя
        String token = Specs.getCurrentToken();

        // Если токен есть (пользователь был создан) - удаляем аккаунт
        if (token != null) {
            try {
                AuthClient.deleteAccount(token);
            } catch (Exception e) {
                // Игнорируем ошибки при удалении (например, если тест уже удалил пользователя)
                System.err.println("Warning: Failed to delete test user: " + e.getMessage());
            }
        }

        // Очищаем авторизацию для следующего теста
        Specs.clearAuth();
    }
}
