package ru.yandex.practicum.filmorate.exceptions.user;

public class RecommendationsNotFound extends RuntimeException {
    public RecommendationsNotFound(String message) {
        super(message);
    }
}
