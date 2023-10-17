package ru.yandex.practicum.filmorate.service.implservice;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.dao.EventDao;


@Service
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventDao eventDao;
    private final UserServiceImpl userService;

    public List<Event> listUserEvents(Long id) { // получение всех событий
        userService.findById(id);
        return this.eventDao.listEvents(id);
    }


    public Event addEvent(Long userId, Long entityId, String eventType, String operationType) { // добавление события
        userService.findById(userId);
        return this.eventDao.addEvent(userId, entityId, eventType, operationType);
    }
}
