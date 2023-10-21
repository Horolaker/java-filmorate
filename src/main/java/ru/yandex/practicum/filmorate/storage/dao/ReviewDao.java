package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    /**
     * Сохранить обзор
     */
    Optional<Review> save(Review review);

    /**
     * Обновить обзор
     */
    Optional<Review> update(Review review);

    /**
     * Удалить обзор
     */
    void delete(Long id);

    /**
     * Получить обзор по Id review
     */
    Optional<Review> getReviewById(Long reviewId);

    /**
     * Добавить лайк обзору
     */
    void addLikeReview(Long reviewDao, Long userId);

    /**
     * Удалить лайк у обзора
     */
    void deleteLikeReview(Long reviewId, Long userId);

    /**
     * Добавить дизлайк обзору
     */
    void addDislikeReview(Long reviewId, Long userId);

    /**
     * Удалить дизллайк у обзора
     */
    void deleteDislikeReview(Long reviewId, Long userId);

    /**
     * Получить список обзоров у фильма
     */
    List<Review> getReviewsByFilmIdLimited(Long filmId, Integer count);

    /**
     * Получить обзоры по film_id
     */
    List<Review> getReviewsByFilmId(Long filmId);

    /**
     *
     */
    List<Review> getLimitedReviews(Integer count);

    /**
     *
     */
    List<Review> getAllReviews();
}
