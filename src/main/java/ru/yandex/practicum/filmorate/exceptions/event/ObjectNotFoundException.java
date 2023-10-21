package ru.yandex.practicum.filmorate.exceptions.event;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
