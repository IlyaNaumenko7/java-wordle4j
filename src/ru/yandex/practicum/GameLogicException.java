package ru.yandex.practicum;

// Наследуем от RuntimeException, так как это "непроверяемое" исключение
public class GameLogicException extends RuntimeException {
    public GameLogicException(String message) {
        super(message);
    }
}