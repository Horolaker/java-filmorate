package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.sqloperation.ReviewSqlOperation.*;

@Repository
public class ReviewDaoImpl implements ReviewDao {
    private static final String REVIEW_TABLE_NAME = "reviews";
    private static final String REVIEW_TABLE_ID_COLUMN_NAME = "review_id";
    private final JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    private FilmDao filmDao;

    @Autowired
    @Qualifier("userDaoImpl")
    public void setUserDao(UserDao userStorage) {
        this.userDao = userStorage;
    }

    @Autowired
    @Qualifier("filmDaoImpl")
    public void setFilmDao(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Review> save(Review review) {
        userIdExistsValidation(review.getUserId());
        filmExistsValidation(review.getFilmId());
        reviewInsertAndSetId(review);
        return Optional.of(review);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Review> update(Review review) {
        userIdExistsValidation(review.getUserId());
        filmExistsValidation(review.getFilmId());

        jdbcTemplate.update(UPDATE_REVIEW.getQuery(),
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        return getReviewById(review.getReviewId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_REVIEW_DISLIKES.getQuery(), id);
        jdbcTemplate.update(DELETE_REVIEW.getQuery(), id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(GET_REVIEW_BY_REVIEW_ID.getQuery(), new ReviewMapper(), reviewId));
        } catch (DataAccessException e) {
            throw new ReviewNotFoundException("Отзыв не найден" + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLikeReview(Long reviewId, Long userId) {
        if (!isLike(reviewId, userId)) {
            jdbcTemplate.update(CREATE_LIKE.getQuery(), reviewId, userId);
//            Если есть дизлайк то прибавляем два оценке(одно из-за дизлайка, второй добавление лайка) и удаляем из
//            таблицы "review_dislike" запись с дизлайком. в противном случае просто прибавляем к рейтингу один
            if (isDislike(reviewId, userId)) {
                jdbcTemplate.update(ADD_TWO_USEFUL.getQuery(), reviewId);
                jdbcTemplate.update(DELETE_DISLIKE.getQuery(), reviewId, userId);
            } else {
                jdbcTemplate.update(ADD_ONE_USEFUL.getQuery(), reviewId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLikeReview(Long reviewId, Long userId) {
        if (isLike(reviewId, userId)) {
            jdbcTemplate.update(DELETE_LIKE.getQuery(), reviewId, userId);
            jdbcTemplate.update(ADD_ONE_USEFUL.getQuery(), reviewId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDislikeReview(Long reviewId, Long userId) {
        if (!isDislike(reviewId, userId)) {
            jdbcTemplate.update(CREATE_DISLIKE.getQuery(), reviewId, userId);
//            Если есть лайк то вычитаем два от оценки(одно из-за лайка, второй вычитание дизлайка) и удаляем из
//            таблицы "review_like" запись с лайком. в противном случае просто отнимаем от рейтинга один
            if (isLike(reviewId, userId)) {
                jdbcTemplate.update(SUBTRACT_TWO_USEFUL.getQuery(), reviewId);
                jdbcTemplate.update(DELETE_LIKE.getQuery(), reviewId, userId);
            } else {
                jdbcTemplate.update(SUBTRACT_ONE_USEFUL.getQuery(), reviewId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDislikeReview(Long reviewId, Long userId) {
        if (isDislike(reviewId, userId)) {
            jdbcTemplate.update(DELETE_DISLIKE.getQuery(), reviewId, userId);
            jdbcTemplate.update(ADD_ONE_USEFUL.getQuery(), reviewId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getReviewsByFilmIdLimited(Long filmId, Integer count) {
        return jdbcTemplate.query(GET_SORT_REVIEW_BY_FILM_ID_WITH_COUNT.getQuery(), new ReviewMapper(), filmId, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getReviewsByFilmId(Long filmId) {
        return jdbcTemplate.query(GET_SORT_REVIEW_BY_FILM_ID.getQuery(), new ReviewMapper(), filmId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getLimitedReviews(Integer count) {
        return jdbcTemplate.query(GET_SORT_REVIEW_WITH_COUNT.getQuery(), new ReviewMapper(), count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getAllReviews() {
        return jdbcTemplate.query(GET_SORT_ALL_REVIEW.getQuery(), new ReviewMapper());
    }

    private boolean isLike(Long reviewId, Long userId) {
        return 1 == jdbcTemplate.queryForObject(IS_LIKE.getQuery(), Integer.class, reviewId, userId);
    }

    private boolean isDislike(Long reviewId, Long userId) {
        return 1 == jdbcTemplate.queryForObject(IS_DISLIKE.getQuery(), Integer.class, reviewId, userId);
    }

    private SimpleJdbcInsert getReviewJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(REVIEW_TABLE_NAME)
                .usingGeneratedKeyColumns(REVIEW_TABLE_ID_COLUMN_NAME);
    }

    private void reviewInsertAndSetId(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = getReviewJdbcInsert();
        long userId = simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue();
        review.setReviewId(userId);
    }

    private void userIdExistsValidation(Long userId) {
        if (userId == null || userDao.getUserById(userId).isEmpty()) {
            throw new UserNotExistException("Пользователь с id: " + userId + " не найден");
        }
    }

    private void filmExistsValidation(Long filmId) {
        if (filmId == null || filmDao.getFilmById(filmId).isEmpty()) {
            throw new FilmNotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }
}
