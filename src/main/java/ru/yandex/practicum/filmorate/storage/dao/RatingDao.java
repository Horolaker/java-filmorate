package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingDao {

    /**
     * Получить рейтинг
     */
    List<Rating> getAllRatings();

    /**
     * Получить рейтинг по id
     */
    Optional<Rating> getRatingById(Long ratingId);
}