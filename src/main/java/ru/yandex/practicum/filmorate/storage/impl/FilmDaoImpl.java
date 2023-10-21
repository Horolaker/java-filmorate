package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UnknownSortTypeException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.sqloperation.FilmSqlOperation.*;
import static ru.yandex.practicum.filmorate.storage.sqloperation.DirectorSqlOperation.*;

@Repository
public class FilmDaoImpl implements FilmDao {
    private static final String FILM_TABLE_NAME = "films";
    private static final String FILM_TABLE_ID_COLUMN_NAME = "film_id";
    private static final String GENRE_QUALIFIER = "genreDaoImpl";
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final DirectorDaoImpl directorDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier(GENRE_QUALIFIER) GenreDao genreDao, DirectorDaoImpl directorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.directorDao = directorDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> findAll() {
        List<Film> foundedFilm = jdbcTemplate.query(GET_ALL_FILMS.getQuery(), new FilmMapper());
        return addGenreAndDirectorToFilms(foundedFilm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Film save(Film film) {
        filmInsertAndSetId(film);
        addGenresToFilm(film);
        directorDao.addDirectorToFilm(film);
        return film;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Film> update(Film film) {
        jdbcTemplate.update(UPDATE_FILM.getQuery(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        setGenresToFilm(film);
        directorDao.deleteDirectorsFromFilm(film.getId());
        directorDao.addDirectorToFilm(film);
        return Optional.of(film);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long filmId) {
        try {
            jdbcTemplate.update(DELETE_FILM.getQuery(), filmId);
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Фильм не найден" + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Film> getFilmById(Long filmId) {
        Optional<Film> film = getValidFilmByFilmId(filmId);
        if (film.isPresent()) {
            film.get().setGenres(genreDao.getGenresByFilmId(filmId));
            film.get().getFilmLikes().addAll(new HashSet<>(getLikesByFilmId(filmId)));
            film.get().setDirectors(directorDao.getDirectorByFilmId(filmId));
        }
        return film;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> getSortedDirectorFilms(Long directorId, String sortBy) {
        List<Film> foundedFilm;
        switch (sortBy) {
            case "year":
                foundedFilm = jdbcTemplate.query(GET_DIRECTOR_FILMS_SORTED_BY_YEAR.getQuery(), new FilmMapper(), directorId);
                return addGenreAndDirectorToFilms(foundedFilm);
            case "likes":
                foundedFilm = jdbcTemplate.query(GET_DIRECTOR_FILMS_SORTED_BY_LIKES.getQuery(), new FilmMapper(), directorId);
                return addGenreAndDirectorToFilms(foundedFilm);
            default:
                throw new UnknownSortTypeException("Unknown Sort Type");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLike(long filmId, long userId) {
        if (!isLikeExistsInFilm(filmId, userId)) {
            jdbcTemplate.update(CREATE_LIKE.getQuery(), filmId, userId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(DELETE_LIKE.getQuery(), filmId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> getSortedFilmsByLikes(Long count) {
        List<Film> sortedFilm = jdbcTemplate.query(GET_MOST_POPULAR_FILMS.getQuery(), new FilmMapper(), count);
        for (Film film : sortedFilm) {
            if (film.getGenres() == null) {
                film.setGenres(genreDao.getGenresByFilmId(film.getId()));
            }
        }
        return sortedFilm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getLikesByFilmId(long filmId) {
        return jdbcTemplate.queryForList(GET_USER_LIKES_BY_FILM_ID.getQuery(), Long.class, filmId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> searchFilmsByDirector(String director) {
        List<Film> foundedFilms = jdbcTemplate.query(SEARCH_FILM_BY_DIRECTOR.getQuery(), new FilmMapper(), "%" + director + "%");
        return addGenreAndDirectorToFilms(foundedFilms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> searchFilmsByTitle(String title) {
        List<Film> foundedFilms = jdbcTemplate.query(SEARCH_FILM_BY_TITLE.getQuery(), new FilmMapper(), "%" + title + "%");
        return addGenreAndDirectorToFilms(foundedFilms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> searchFilmsByDirectorAndTitle(String query) {
        List<Film> foundedFilms = jdbcTemplate.query(SEARCH_FILM_BY_DIRECTOR_AND_TITLE.getQuery(), new FilmMapper(), "%" + query + "%", "%" + query + "%");
        return addGenreAndDirectorToFilms(foundedFilms);
    }

    private Optional<Film> getValidFilmByFilmId(Long filmId) {
        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(GET_FILM_BY_FILM_ID.getQuery(), new FilmMapper(), filmId));
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Фильм не найден" + e.getMessage());
        }
    }

    private boolean isLikeExistsInFilm(long filmId, long userId) {
        return getLikesByFilmId(filmId).contains(userId);
    }

    private void filmInsertAndSetId(Film film) {
        long filmId = getFilmSimpleJdbcInsert().executeAndReturnKey(film.toMap()).longValue();
        film.setId(filmId);
    }

    private SimpleJdbcInsert getFilmSimpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILM_TABLE_NAME)
                .usingGeneratedKeyColumns(FILM_TABLE_ID_COLUMN_NAME);
    }

    private void addGenresToFilm(Film film) {
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(CREATE_FILM_GENRE.getQuery(), film.getId(), genre.getId());
            }
        }
    }

    private void setGenresToFilm(Film film) {
        try {
            List<Genre> genresFromDbByFilm = genreDao.getGenresIdByFilmId(film.getId());
            if (!film.getGenres().isEmpty()) {
                List<Genre> genresFromUI = new ArrayList<>(film.getGenres());
                if (genresFromDbByFilm.isEmpty()) {
                    updateFilmGenres(genresFromUI, film);
                } else {
                    List<Genre> matchedGenres = getGenresMatch(genresFromUI, genresFromDbByFilm);
                    genresFromUI.removeAll(matchedGenres);
                    genresFromDbByFilm.removeAll(matchedGenres);
                    deleteFilmGenresFromDb(genresFromDbByFilm);
                    updateFilmGenres(genresFromUI, film);
                }
            } else {
                deleteFilmGenresFromDb(genresFromDbByFilm);
            }
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Жанр не найден" + e.getMessage());
        }
    }

    private List<Genre> getGenresMatch(List<Genre> genres1, List<Genre> genres2) {
        return genres1.stream()
                .filter(front -> genres2.stream().anyMatch(db -> db.getId() == front.getId()))
                .collect(Collectors.toList());
    }

    private void deleteFilmGenresFromDb(List<Genre> genres) {
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(DELETE_FILM_GENRES_BY_GENRE_ID.getQuery(), genre.getId());
            }
        }
    }

    private void updateFilmGenres(List<Genre> genresFromFrontEnd, Film film) {
        if (!genresFromFrontEnd.isEmpty()) {
            jdbcTemplate.batchUpdate(CREATE_FILM_GENRE.getQuery(), new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genresFromFrontEnd.get(i);
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return genresFromFrontEnd.size();
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        List<Film> commonFilm = jdbcTemplate.query(GET_COMMON_FILMS.getQuery(), new FilmMapper(), userId, friendId);
        for (Film film : commonFilm) {
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<Genre>());
            }
        }
        return commonFilm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> getPopularFilms(long genreId, int year, int count) {
        List<Film> popularFilm = jdbcTemplate.query(GET_POPULAR_FILMS.getQuery(),
                new FilmMapper(), year, year, genreId, genreId, count);
        for (Film film : popularFilm) {
            List<Genre> genres = jdbcTemplate.query(GET_FILMS_GENRES.getQuery(), new GenreMapper(), film.getId());
            List<Director> directors = jdbcTemplate.query(GET_DIRECTOR_BY_FILM_ID.getQuery(), new DirectorMapper(), film.getId());
            film.setGenres(genres);
            film.setDirectors(directors);
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<Genre>());
            }
            if (film.getDirectors() == null) {
                film.setDirectors(new ArrayList<>());
            }
        }
        return popularFilm;
    }

    private List<Film> addGenreAndDirectorToFilms(List<Film> films) {
        for (Film film : films) {
            if (film.getGenres() == null) {
                film.setGenres(genreDao.getGenresByFilmId(film.getId()));
                film.setDirectors(directorDao.getDirectorByFilmId(film.getId()));
            }
        }
        return films;
    }
}
