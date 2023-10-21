package ru.yandex.practicum.filmorate.controllers.implcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.implservice.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.implservice.FilmServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmImplController {

    private final FilmServiceImpl filmService;
    private final EventServiceImpl eventService;

    public FilmImplController(FilmServiceImpl filmService, EventServiceImpl eventService) {
        this.filmService = filmService;
        this.eventService = eventService;
    }

    /**
     * Получить список всех фильмов
     */
    @GetMapping
    public List<Film> findAll() {
        log.debug("Пришел запрос GET /films");
        List<Film> foundedFilms = filmService.findAll();
        log.debug("Отправлен овтет на GET запрос /films с телом: {}", foundedFilms);
        return foundedFilms;
    }

    /**
     * Получить фильм по id
     */
    @GetMapping("/{filmId}")
    public Film findById(@PathVariable("filmId") @Min(0) Long filmId) {
        log.info("Получен GET-запрос /films/{}", filmId);
        Optional<Film> foundedFilm = filmService.findById(filmId);
        log.info("Отправлен ответ на GET-запрос /films/{} с телом: {}", filmId, foundedFilm);
        return foundedFilm.get();
    }

    /**
     * Создать film
     */
    @PostMapping
    public Film save(@RequestBody @Valid Film film) {
        log.info("Пришел POST-запрос /films с телом: {}", film);
        Film createdFilm = filmService.save(film);
        log.info("Отправлен ответ на POST-запрос /films с телом: {}", createdFilm);
        return createdFilm;
    }

    /**
     * Обновить фильм
     */
    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Пришел PUT-запрос /films с телом: {}", film);
        Optional<Film> updatedFilm = filmService.update(film);
        log.info("Отправлен ответ на PUT-запрос /films с телом: {}", updatedFilm);
        return updatedFilm.get();
    }

    /**
     * Удалить фильм
     */
    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable("filmId") @Min(0) Long filmId) {
        log.info("Получен DELETE-запрос /films/{}", filmId);
        log.info("Отправлен ответ на PUT-запрос /films/{}", filmId);
        filmService.delete(filmId);
    }

    /**
     * Возвращает список фильмов режиссера отсортированных по количеству лайков или году выпуска
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getSortedDirectorFilms(@PathVariable Long directorId,
                                             @RequestParam(defaultValue = "year") String sortBy) {
        log.info("Получен GET-запрос /films/director/{}/?sortBy={}", directorId, sortBy);
        return filmService.getSortedDirectorFilms(directorId, sortBy);
    }

    /**
     * Добавить лайк фильму
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") @Min(0) Long filmId,
                        @PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен PUT-запрос /films/{}/like/{}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Отправлен ответ на PUT-запрос /films/{}/like/{}", filmId, userId);
        eventService.addEvent(userId, filmId, 2, "ADD");

    }

    /**
     * Удалить лайк
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") @Min(0) Long filmId,
                           @PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен DELETE-запрос /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
        log.info("Отправлен ответ на DELETE-запрос /films/{}/like/{}", filmId, userId);
        eventService.addEvent(userId, filmId, 2, "REMOVE");

    }

    /**
     * вывод общих с другом фильмов с сортировкой по их популярности
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.info("Получен GET-запрос films/common?userId={userId}&friendId={friendId} с id {} " +
                "и otherId {}", userId, friendId);
        List<Film> foundedCommonFilms = filmService.getCommonFilms(userId, friendId);
        log.info("Отправлен ответ на GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {} c телом {}", userId, friendId, foundedCommonFilms);
        return foundedCommonFilms;
    }

    /**
     * Возвращает список самых популярных фильмов указанного жанра за нужный год
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                      @RequestParam(value = "genreId", required = false, defaultValue = "-1") Long genreId,
                                      @RequestParam(value = "year", required = false, defaultValue = "-1") Integer year) {
        log.info("Получен GET-запрос  /films/popular?count={limit}&genreId={genreId}&year={year} с genreId {} " +
                " year {} и count {}", genreId, year, count);
        List<Film> foundedPopularFilms = filmService.getPopularFilms(genreId, year, count);
        log.info("Отправлен ответ на GET-запрос  /films/popular?count={limit}&genreId={genreId}&year={year} " +
                "с genreId {} year {} и count {} c телом {}", genreId, year, count, foundedPopularFilms);
        return foundedPopularFilms;
    }

    /**
     * поиск по названию фильмов и по режиссёру
     */
    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam String by) {
        log.info("Получен GET-запрос /films/search?query={}&by={}", query, by);
        List<Film> filmsList = filmService.searchFilms(query, by);
        log.info("Отправлен ответ на GET-запрос /films/search?query={}&by={} c телом {}", query, by, filmsList);
        return filmsList;
    }
}
