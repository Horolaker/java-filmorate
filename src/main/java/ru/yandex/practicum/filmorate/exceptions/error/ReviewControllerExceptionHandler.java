package ru.yandex.practicum.filmorate.exceptions.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.implcontrollers.ReviewImplController;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.io.FileNotFoundException;

@RestControllerAdvice(assignableTypes = {ReviewImplController.class})
public class ReviewControllerExceptionHandler {
    @ExceptionHandler({UserNotExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotExistExceptionUser(final UserNotExistException e) {
        return new ErrorResponse("Ошибка поиска пользователя. ", e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptionUser(final UserNotFoundException e) {
        return new ErrorResponse("Ошибка поиска пользователя. ", e.getMessage());
    }

    @ExceptionHandler({FilmNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExistExceptionFilm(final FilmNotExistException e) {
        return new ErrorResponse("Ошибка поиска фильма. ", e.getMessage());
    }

    @ExceptionHandler({ReviewNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExistExceptionFilm(final ReviewNotFoundException e) {
        return new ErrorResponse("Ошибка поиска отзыва. ", e.getMessage());
    }


    @ExceptionHandler({FileNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundExceptionFilm(final FileNotFoundException e) {
        return new ErrorResponse("Ошибка поиска фильма. ", e.getMessage());
    }
}
