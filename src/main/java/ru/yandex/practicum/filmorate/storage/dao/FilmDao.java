package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    /**
     * Найти и получить все фильмы
     */
    List<Film> findAll();

    /**
     * Сохранить фильм
     */
    Film save(Film film);

    /**
     * Обновить фильм
     */
    Optional<Film> update(Film film);

    /**
     * Удалить фильм по id
     */
    void delete(Long id);

    /**
     * Получить фильм по id
     */
    Optional<Film> getFilmById(Long id);

    /**
     * Получить сортированный по режиссерам список фильмов
     */
    List<Film> getSortedDirectorFilms(Long directorId, String sortBy);

    /**
     * Поставить лайк
     */
    void addLike(long filmId, long userId);

    /**
     * Удалить лайк
     */
    void deleteLike(long filmId, long userId);

    /**
     * Получить сортированный по лайкам список фильмов
     */
    List<Film> getSortedFilmsByLikes(Long count);

    /**
     * Получить список лайков по Id фильма
     */
    List<Long> getLikesByFilmId(long filmId);

    /**
     * Получить список фильмов по режиссеру
     */
    List<Film> searchFilmsByDirector(String director);

    /**
     * Получить список фильмов по названию
     */
    List<Film> searchFilmsByTitle(String title);

    /**
     * Получить список фильмов по режиссеру и названию
     */
    List<Film> searchFilmsByDirectorAndTitle(String query);

    /**
     * Получить пересекающиеся фильмы
     */
    List<Film> getCommonFilms(long userId, long friendId);

    /**
     * Получить популярные фильмы
     */
    List<Film> getPopularFilms(long genreId, int year, int count);

}
