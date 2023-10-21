package ru.yandex.practicum.filmorate.exceptions;

public class UnknownSortTypeException extends RuntimeException {
    public UnknownSortTypeException(String message) {
            super(message);
        }
}
