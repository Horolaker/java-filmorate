package ru.yandex.practicum.filmorate.storage.impl;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.dao.RatingDao;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.sqloperation.RatingSqlOperation.GET_ALL_RATINGS;
import static ru.yandex.practicum.filmorate.storage.sqloperation.RatingSqlOperation.GET_RATING_BY_RATING_ID;

@Repository
public class RatingDaoImpl implements RatingDao {

    private final JdbcTemplate jdbcTemplate;

    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Rating> getAllRatings() {
        return jdbcTemplate.query(GET_ALL_RATINGS.getQuery(), new RatingMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Rating> getRatingById(Long ratingId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(GET_RATING_BY_RATING_ID.getQuery(), new RatingMapper(), ratingId));
        } catch (DataAccessException e) {
            throw new RatingNotFoundException("Рейтинг не найден" + e.getMessage());
        }
    }
}
