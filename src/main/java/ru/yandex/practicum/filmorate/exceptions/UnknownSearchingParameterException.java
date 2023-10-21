package ru.yandex.practicum.filmorate.exceptions;

public class UnknownSearchingParameterException extends RuntimeException {
    public UnknownSearchingParameterException(String message) {
        super(message);
    }
}
