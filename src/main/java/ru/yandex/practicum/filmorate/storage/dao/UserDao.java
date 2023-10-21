package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    /**
     * Найти всех юзеров
     */
    List<User> findAll();

    /**
     * Сохранить юзера
     */
    Optional<User> save(User user);

    /**
     * Обновить юзера
     */
    Optional<User> update(User user);

    /**
     * Удалить юзера по id
     */
    void delete(Long id);

    /**
     * Получить юзера по id
     */
    Optional<User> getUserById(Long id);
}