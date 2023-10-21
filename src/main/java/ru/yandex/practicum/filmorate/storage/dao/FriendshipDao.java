package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {

    /**
     * Добавить друга
     */
    void addFriend(long userId, long friendId);

    /**
     * Удалить друга
     */
    void deleteFriend(long userId, long friendId);

    /**
     * Получить список друзей по id юзера
     */
    List<User> getFriends(long userId);

    /**
     * Получить общих друзей
     */
    List<User> getCommonFriends(long userId, long friendId);
}
