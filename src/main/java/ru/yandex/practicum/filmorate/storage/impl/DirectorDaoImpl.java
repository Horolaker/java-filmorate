package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDao;

import static ru.yandex.practicum.filmorate.storage.sqloperation.DirectorSqlOperation.*;

import java.util.List;
import java.util.Map;

@Component("directorDaoImpl")
@AllArgsConstructor
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director").usingGeneratedKeyColumns("id");
        director.setId(simpleJdbcInsert.executeAndReturnKey(Map.of("name", director.getName())).longValue());
        return director;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Director updateDirector(Director director) {
        jdbcTemplate.update(UPDATE_DIRECTOR.getQuery(),
                director.getName(),
                director.getId());
        return director;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDirector(Long directorId) {
        jdbcTemplate.update(DELETE_DIRECTOR.getQuery(), directorId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Director> getDirectorsList() {
        return jdbcTemplate.query(GET_ALL_DIRECTORS.getQuery(), new DirectorMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Director getDirectorById(Long directorId) {
        return jdbcTemplate.queryForObject(GET_DIRECTOR_BY_ID.getQuery(),
                new DirectorMapper(), directorId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Director> getDirectorByFilmId(Long filmId) {
        if (jdbcTemplate.queryForList(GET_DIRECTOR_BY_FILM_ID.getQuery(), filmId).isEmpty()) {
            return List.of();
        }
        return jdbcTemplate.query(GET_DIRECTOR_BY_FILM_ID.getQuery(), new DirectorMapper(), filmId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkDirectorExistInDb(Long id) {
        return !jdbcTemplate.query(GET_DIRECTOR_BY_ID.getQuery(), new DirectorMapper(), id).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDirectorToFilm(Film film) {
        if (film.getDirectors() != null) {
            if (!film.getDirectors().isEmpty()) {
                for (Director director : film.getDirectors()) {
                    jdbcTemplate.update(ADD_DIRECTOR_TO_FILM.getQuery(), film.getId(), director.getId());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDirectorsFromFilm(Long filmId) {
        jdbcTemplate.update(DELETE_DIRECTOR_FROM_FILM.getQuery(), filmId);
    }
}
