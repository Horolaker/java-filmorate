package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long friendId);
}
