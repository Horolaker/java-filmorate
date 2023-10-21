package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    /**
     * Получить список жанров
     */
    List<Genre> getGenres();

    /**
     * Получить жанр по id
     */
    Optional<Genre> getGenreById(Long genreId);

    /**
     * Получить жанры по id фильма
     */
    List<Genre> getGenresByFilmId(Long filmId);

    /**
     * Получить жанры по id фильма
     */
    List<Genre> getGenresIdByFilmId(Long filmId);
}
