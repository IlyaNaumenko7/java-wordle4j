package ru.yandex.practicum;

// Наследуем от Exception, так как это "проверяемое" исключение (обязываем обрабатывать)
public class WordNotFoundException extends Exception {
    public WordNotFoundException(String message) {
        super(message);
    }
}