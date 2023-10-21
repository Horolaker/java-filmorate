package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.RecommendationDao;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.sqloperation.RecommendationSqlOperation.GET_FILMS_RECOMMENDATION;

@Repository
@RequiredArgsConstructor
public class RecommendationDaoImpl implements RecommendationDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Film> getRecommendation(Long userId) {

        List<Film> films = new ArrayList<Film>();
        try {
            films = jdbcTemplate.query(GET_FILMS_RECOMMENDATION.getQuery(), new FilmMapper(),
                    userId, userId, userId);
            for (Film film : films) {
                if (film.getGenres() == null) {
                    film.setGenres(genreDao.getGenresByFilmId(film.getId()));
                }
            }
        } catch (DataAccessException e) {
            System.out.println("exception on deleting store - " + e);
            return films;
        }


        return films;
    }
}
