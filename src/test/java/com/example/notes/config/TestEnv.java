package com.example.notes.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс для удобного доступа к настройкам тестового окружения.
 *
 * ЗАЧЕМ НУЖЕН ЭТОТ КЛАСС:
 * Этот класс - это обёртка (wrapper) над библиотекой Owner.
 * Вместо того, чтобы в каждом тесте писать:
 *   ConfigFactory.create(EnvConfig.class).baseUrl()
 * Мы просто пишем:
 *   TestEnv.getBaseUrl()
 *
 * ЭТО УПРОЩАЕТ:
 * 1. Код тестов становится короче и читабельнее
 * 2. Не нужно импортировать ConfigFactory и создавать объект конфигурации в каждом тесте
 * 3. Централизованное место для работы с конфигурацией
 *
 * ПАТТЕРН ПРОЕКТИРОВАНИЯ:
 * Это пример паттерна "Фасад" (Facade) - мы скрываем сложную логику Owner
 * за простыми статическими методами.
 *
 * ПОЧЕМУ СТАТИЧЕСКИЕ МЕТОДЫ:
 * - Статические методы можно вызывать без создания объекта класса: TestEnv.getBaseUrl()
 * - Конфигурация одна на все тесты, не нужно создавать множество объектов
 * - Это удобно: импортируем класс один раз и используем везде
 */
public class TestEnv {

    /**
     * Единственный экземпляр конфигурации для всех тестов.
     *
     * ЧТО ЗДЕСЬ ПРОИСХОДИТ:
     * ConfigFactory.create(EnvConfig.class) - это магический вызов библиотеки Owner.
     * Owner берёт наш интерфейс EnvConfig и создаёт его реализацию:
     * 1. Читает файл local.properties (путь указан в @Config.Sources)
     * 2. Парсит содержимое файла (формат ключ=значение)
     * 3. Создаёт объект, который реализует методы baseUrl() и logLevel()
     * 4. При вызове методов возвращает значения из файла или @DefaultValue
     *
     * ПОЧЕМУ STATIC FINAL:
     * - static: переменная принадлежит классу, а не объекту
     * - final: значение устанавливается один раз и не может быть изменено
     * - Это гарантирует, что конфигурация создаётся только один раз при загрузке класса
     *
     * ПОЧЕМУ PRIVATE:
     * Мы не хотим, чтобы внешний код напрямую обращался к CONFIG.
     * Вместо этого используем публичные методы getBaseUrl() и getLogLevel().
     * Это инкапсуляция - скрываем детали реализации.
     */
    private static final EnvConfig CONFIG = ConfigFactory.create(EnvConfig.class);

    /**
     * Возвращает базовый URL API для тестирования.
     *
     * ЗАЧЕМ ЭТОТ МЕТОД:
     * Предоставляет простой способ получить URL сервера из любого теста.
     * Вместо прямого обращения к CONFIG используем этот метод.
     *
     * ПРИМЕР ИСПОЛЬЗОВАНИЯ В ТЕСТЕ:
     * <pre>
     * public class MyTest {
     *     @Test
     *     public void testAPI() {
     *         String url = TestEnv.getBaseUrl(); // Получаем URL
     *         given().baseUri(url).when().get("/health").then().statusCode(200);
     *     }
     * }
     * </pre>
     *
     * ОТКУДА БЕРЁТСЯ ЗНАЧЕНИЕ:
     * 1. Из файла src/test/resources/config/local.properties (если там есть baseUrl=...)
     * 2. Если файла нет или ключа нет - используется значение по умолчанию
     *    из @DefaultValue в EnvConfig
     *
     * @return базовый URL API (например, "https://practice.expandtesting.com/notes/api")
     */
    public static String getBaseUrl() {
        return CONFIG.baseUrl();
    }

    /**
     * Возвращает уровень логирования HTTP-запросов и ответов.
     *
     * ЗАЧЕМ ЭТОТ МЕТОД:
     * Позволяет настроить, насколько подробно RestAssured будет выводить информацию
     * о запросах в консоль при запуске тестов.
     *
     * ПРИМЕР ИСПОЛЬЗОВАНИЯ В ТЕСТЕ:
     * <pre>
     * public class MyTest {
     *     @BeforeAll
     *     public static void setup() {
     *         RestAssured.baseURI = TestEnv.getBaseUrl();
     *         // Настраиваем логирование на основе конфигурации
     *         String logLevel = TestEnv.getLogLevel();
     *         if ("ALL".equals(logLevel)) {
     *             RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
     *         }
     *     }
     * }
     * </pre>
     *
     * ВОЗМОЖНЫЕ ЗНАЧЕНИЯ:
     * - "NONE" - ничего не логировать
     * - "BASIC" - только метод, URL и статус код (по умолчанию)
     * - "HEADERS" - плюс все заголовки запроса и ответа
     * - "BODY" - плюс тело запроса и ответа
     * - "ALL" - максимально подробная информация
     *
     * ОТКУДА БЕРЁТСЯ ЗНАЧЕНИЕ:
     * 1. Из файла src/test/resources/config/local.properties (если там есть log.level=...)
     * 2. Если файла нет или ключа нет - используется "BASIC" из @DefaultValue
     *
     * @return уровень логирования (NONE, BASIC, HEADERS, BODY или ALL)
     */
    public static String getLogLevel() {
        return CONFIG.logLevel();
    }
}
