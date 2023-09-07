package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@Slf4j
public class UserController {

    Map<Long, User> users = new HashMap<Long, User>();
    Long iterator = 0L;

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        System.out.println("Start POSTing user...");
        if (validateUser(user)) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.debug("Создан пользователь. Переданные данные: {}", user);
            System.out.println(user);
            return addNewUser(user);
        }
        throw new ValidationException("Validation failed");
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Validation failed");
        }
        if (validateUser(user)) {
            Long checkingUserId = user.getId();
                if (checkingUserId == null) {
                    log.debug("Передан пользователь без ID. Переданные данные: {}", user);
                } else if (user.equals(users.get(checkingUserId))) {
                    log.debug("Существует идентичный пользователь. Переданные данные: {}", user);
                }
            log.debug("Обновлен пользователь. Переданные данные: {}", user);
            users.put(checkingUserId, user);
            return user;
        }
        throw new ValidationException("Validation failed");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.debug("Все пользователи на момент вызова метода: GET /users {}", users);
        return users.values().stream().toList();
    }


    public User addNewUser(User user) {
        user.setId(++iterator);
        users.put(iterator, user);
        return user;
    }

    public boolean validateUser(User user) {
        System.out.println("Start validation");
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate date = user.getBirthday();
        boolean isOK = true;
            if (email == null || email.indexOf("@")==-1) {
                log.debug("Некорректное значение поля email. Переданные данные: {}", email);
                isOK = false;
            }
            if (login == null || login.indexOf(" ")!=-1) {
                log.debug("Некорректное значение поля login. Переданные данные: {}", login);
                isOK = false;
            }
            if (date.isAfter(LocalDate.now())) {
                log.debug("Поле birthday не может быть старше нынешней даты. Переданные данные: {}", date);
                isOK = false;
            }
        return isOK;
    }

}
