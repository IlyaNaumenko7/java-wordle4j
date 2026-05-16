package ru.yandex.practicum;

// Тоже проверяемое исключение
public class DictionaryLoadException extends Exception {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}