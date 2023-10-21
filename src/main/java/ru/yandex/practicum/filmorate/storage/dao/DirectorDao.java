package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorDao {

    /**
     * Создать режисера
     */
    Director createDirector(Director director);

    /**
     * Обновить режисера
     */
    Director updateDirector(Director director);

    /**
     * Удалить режисера
     */
    void deleteDirector(Long directorId);

    /**
     * Получить режисеров
     */
    List<Director> getDirectorsList();

    /**
     * Получить режисера по Id
     */
    Director getDirectorById(Long directorId);

    /**
     * Получить режисера по film_id
     */
    List<Director> getDirectorByFilmId(Long filmId);

    /**
     * Проверка есть ли режисер в Db
     */
    boolean checkDirectorExistInDb(Long id);

    /**
     * Добавить режисера в фильм
     */
    void addDirectorToFilm(Film film);

    /**
     * Удалить режисера из фильма
     */
    void deleteDirectorsFromFilm(Long directorId);
}
