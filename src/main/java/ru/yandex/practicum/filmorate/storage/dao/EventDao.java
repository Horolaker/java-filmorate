package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository
public interface EventDao {

    /**
     * Получить ленту событий по id пользователя
     */
    List<Event> getUserFeed(Long id);

    /**
     * Получение события по id
     */
    Event getEvent(Long id);

    /**
     * Добавить событие
     */
    Event addEvent(Long userId, Long entityId, Integer eventType, String operationType);

}
