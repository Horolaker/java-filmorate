package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDao;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImpl {
    private ReviewDao reviewDao;

    @Autowired
    public void setReviewDao(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }


    public Optional<Review> findById(Long reviewId) {
        return reviewDao.getReviewById(reviewId);
    }

    public Optional<Review> save(Review review) {
        return reviewDao.save(review);
    }

    public Optional<Review> update(Review review) {
        return reviewDao.update(review);
    }

    public void delete(Long reviewId) {
        reviewDao.delete(reviewId);
    }


    public void addLikeReview(Long reviewId, Long userId) {
        reviewDao.addLikeReview(reviewId, userId);
    }

    public void deleteLikeReview(Long reviewId, Long userId) {
        reviewDao.deleteLikeReview(reviewId, userId);
    }

    public void addDislikeReview(Long reviewId, Long userId) {
        reviewDao.addDislikeReview(reviewId, userId);
    }

    public void deleteDislikeReview(Long reviewId, Long userId) {
        reviewDao.deleteDislikeReview(reviewId, userId);
    }

    public List<Review> getReviewsByFilmIdLimited(Long filmId, Integer count) {
        return reviewDao.getReviewsByFilmIdLimited(filmId, count);
    }

    public List<Review> getReviewsByFilmId(Long filmId) {
        return reviewDao.getReviewsByFilmId(filmId);
    }

    public List<Review> getLimitedReviews(Integer count) {
        return reviewDao.getLimitedReviews(count);
    }

    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

}
