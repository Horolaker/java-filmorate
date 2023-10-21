package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.HashMap;
import java.util.Map;

@Validated
@Data
@Builder
public class Event implements Comparable<Event> {
    private Long eventId;
    @NotBlank
    private Long userId;
    @NotBlank
    private Long entityId;
    @NotNull
    private EventType eventType;
    @NotNull
    private OperationType operation;
    @PastOrPresent
    private Long timestamp;

    public Map<String, Object> toMap() {

        Map<String, Object> eventProperties = new HashMap<>();

        eventProperties.put("event_id", eventId);
        eventProperties.put("user_id", userId);
        eventProperties.put("entity_id", entityId);
        eventProperties.put("event_type", eventType.getName());
        eventProperties.put("operation_type", operation.getName());
        eventProperties.put("time_stamp", timestamp);

        return eventProperties;
    }

    public enum EventType {
        FRIEND(1), LIKE(2), REVIEW(3);

        private final int nameId;

        EventType(int nameId) {
            this.nameId = nameId;
        }

        @JsonValue
        public String getName() {
            return this.findById(nameId).toString();
        }

        public static EventType findById(int nameId) {
            for (EventType e : values()) {
                if (e.nameId == nameId) {
                    return e;
                }
            }
            throw new UnsupportedOperationException(String.format("Неизвестный тип события: '%s'", nameId));
        }

        @JsonCreator
        public static EventType fromName(String name) {
            return EventType.valueOf(name);
        }
    }

    public enum OperationType {
        ADD(1), UPDATE(2), REMOVE(3);

        private final int nameId;

        OperationType(int nameId) {
            this.nameId = nameId;
        }

        @JsonValue
        public String getName() {
            return findById(nameId).toString();
        }

        public static OperationType findById(int nameId) {
            for (OperationType e : values()) {
                if (e.nameId == nameId) {
                    return e;
                }
            }
            throw new UnsupportedOperationException(String.format("Неизвестная операция '%s'", nameId));
        }

        @JsonCreator
        public static OperationType fromName(String name) {
            return OperationType.valueOf(name);
        }
    }

    @Override
    public int compareTo(Event otherEvent) {
        return this.getTimestamp().compareTo(otherEvent.getTimestamp());
    }
}

