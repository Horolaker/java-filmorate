package ru.yandex.practicum.filmorate.exceptions.review;

public class ReviewNotExistException extends RuntimeException {
    public ReviewNotExistException(String message) {
        super(message);
    }
}
