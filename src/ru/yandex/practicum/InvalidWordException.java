package ru.yandex.practicum;

// Так же проверяемое исключение
public class InvalidWordException extends Exception {
    public InvalidWordException(String message) {
        super(message);
    }
}