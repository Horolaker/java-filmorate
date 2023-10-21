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

    public List<Event> getUserFeed(Long id) {
        userService.findById(id);
        return this.eventDao.getUserFeed(id);
    }

    public Event addEvent(Long userId, Long entityId, Integer eventType, String operationType) {
        userService.findById(userId);
        return this.eventDao.addEvent(userId, entityId, eventType, operationType);
    }
}
