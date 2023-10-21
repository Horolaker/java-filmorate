package ru.yandex.practicum.filmorate.controllers.implcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.implservice.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.implservice.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserImplController {

    private final UserServiceImpl userService;
    private final EventServiceImpl eventService;

    public UserImplController(UserServiceImpl userService, EventServiceImpl eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    /**
     * Получить список всех user
     */
    @GetMapping
    public List<User> findAll() {
        log.info("Получен GET-запрос /users");
        List<User> foundedUsers = userService.findAll();
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", foundedUsers);
        return foundedUsers;
    }

    /**
     * Получить user по id
     */
    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен GET-запрос /users/{}", userId);
        Optional<User> foundedUser = userService.findById(userId);
        log.info("Отправлен ответ на GET-запрос /users/{} с телом: {}", userId, foundedUser);
        return foundedUser;
    }

    /**
     * Создать пользователя
     */
    @PostMapping
    public Optional<User> save(@RequestBody @Valid User user) {
        log.info("Получен POST-запрос /users с телом: {}", user);
        Optional<User> createdUser = userService.save(user);
        log.info("Отправлен ответ на POST-запрос /users с телом: {}", createdUser);
        return createdUser;
    }

    /**
     * Обновить пользователя
     */
    @PutMapping
    public Optional<User> update(@RequestBody @Valid User user) {
        log.info("Получен PUT-запрос /users с телом: {}", user);
        Optional<User> updatedUser = userService.update(user);
        log.info("Отправлен ответ на PUT-запрос /users с телом: {}", updatedUser);
        return updatedUser;
    }

    /**
     * Удалить пользователя
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен DELETE-запрос /users/{}", userId);
        log.info("Отправлен ответ на DELETE-запрос /users/{}", userId);
        userService.delete(userId);
    }

    /**
     * Добавить друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") @Min(0) Long userId,
                          @PathVariable("friendId") @Min(0) Long friendId) {
        log.info("Получен PUT-запрос /users/{}/friends/{}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Отправлен ответ на PUT-запрос /users/{}/friends/{}", userId, friendId);
        eventService.addEvent(userId, friendId, 1, "ADD");
    }

    /**
     * Удалить друга
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") @Min(0) Long userId,
                             @PathVariable("friendId") @Min(0) Long friendId) {
        log.info("Получен DELETE-запрос /users/{}/friends/{}", userId, friendId);
        log.info("Отправлен ответ на DELETE-запрос /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
        eventService.addEvent(userId, friendId, 1, "REMOVE");
    }

    /**
     * Посмотреть список друзей
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") @Min(0) Long userId) {
        log.info("Получен GET-запрос /users/{id}/friends");
        List<User> friendList = userService.getFriends(userId);
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", friendList);
        return friendList;
    }

    /**
     * Посмотреть общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") @Min(0) Long userId,
                                       @PathVariable("otherId") @Min(0) Long friendId) {
        log.info("Получен GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {}", userId, friendId);
        List<User> foundedCommonFriends = userService.getCommonFriends(userId, friendId);
        log.info("Отправлен ответ на GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {} c телом {}", userId, friendId, foundedCommonFriends);
        return foundedCommonFriends;
    }

    /**
     * Возвращает рекомендации по фильмам для просмотра.
     */
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") @Min(0) Long userId) {
        log.info("Получен GET-запрос /users/{id}/recommendations с id {}" + userId);
        List<Film> recommendedFilms = userService.getRecommendation(userId);
        log.info("Отправлен ответ GET-запрос /users/{id}/recommendations с id {} и телом {}", userId, recommendedFilms);
        return recommendedFilms;
    }

    /**
     * Получить ленту активности
     */
    @GetMapping("/{id}/feed")
    public List<Event> getUserFeed(@RequestBody @PathVariable("id") @Min(0) Long userId) {
        log.info("Получен GET-запрос users/{id}/feed с id {} ", userId);
        return eventService.getUserFeed(userId);
    }
}
