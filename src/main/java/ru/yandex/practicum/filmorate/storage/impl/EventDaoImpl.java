package ru.yandex.practicum.filmorate.storage.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.event.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.dao.EventDao;

import static ru.yandex.practicum.filmorate.storage.sqloperation.EventSqlOperation.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventDaoImpl implements EventDao {

    private final EventMapper eventMapper;
    private final JdbcTemplate jdbcTemplate;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> getUserFeed(Long id) {
        List<Event> events = jdbcTemplate.query(GET_EVENT_BY_USER_ID.getQuery(), eventMapper, id);
        String result = events.stream()
                .map(Event::toString)
                .collect(Collectors.joining(", "));
        log.info("Список событий по запросу: {}", result);
        return events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event addEvent(Long userId, Long entityId, Integer eventType, String operationType) {

        SimpleJdbcInsert eventInsertion = new SimpleJdbcInsert(jdbcTemplate).withTableName("events")
                .usingGeneratedKeyColumns("event_id");

        Event event = Event.builder()
                .userId(userId)
                .entityId(entityId)
                .eventType(Event.EventType.findById(eventType))
                .operation(Event.OperationType.fromName(operationType))
                .timestamp(System.currentTimeMillis())
                .build();
        Long eventId = eventInsertion.executeAndReturnKey(event.toMap()).longValue();

        Event newEvent = getEvent(eventId);
        log.info("Создано событие: {} ", newEvent);

        return newEvent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event getEvent(Long id) {
        checkEventId(id);
        String sqlEvent = "SELECT * FROM events WHERE event_id = ?";
        Event event = jdbcTemplate.queryForObject(GET_EVENT_BY_EVENT_ID.getQuery(), eventMapper, id);
        log.info("Найдено событие: {} ", id);
        return event;
    }

    public void checkEventId(Long eventId) {

        SqlRowSet sqlId = jdbcTemplate
                .queryForRowSet(SELECT_EVENT_ID.getQuery(), eventId);

        if (!sqlId.next()) {
            log.info("Событие с идентификатором {} не найдено.", eventId);
            throw new ObjectNotFoundException(String.format("Событие с id: %d не найдено", eventId));
        }
    }
}
