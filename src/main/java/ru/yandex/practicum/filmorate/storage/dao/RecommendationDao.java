package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface RecommendationDao {

    /**
     * Получить список фильмов в рекоммендации
     */
    public List<Film> getRecommendation(Long userId);

}
