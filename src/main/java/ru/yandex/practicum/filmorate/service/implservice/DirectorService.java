package ru.yandex.practicum.filmorate.service.implservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.director.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDao;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DirectorService {

    private final DirectorDao directorDao;

    public Director createDirector(Director director) {
        validateDirectorsName(director);
        return directorDao.createDirector(director);
    }

    public Director updateDirector(Director director) {
        if (directorDao.checkDirectorExistInDb(director.getId())) {
            return directorDao.updateDirector(director);
        } else {
            throw new DirectorNotFoundException("Режиссёр не найден");
        }
    }

    public Director getDirectorById(Long id) {
        if (directorDao.checkDirectorExistInDb(id)) {
            return directorDao.getDirectorById(id);
        } else {
            throw new DirectorNotFoundException("Режиссёр не найден");
        }
    }

    public List<Director> getDirectorsList() {
        return directorDao.getDirectorsList();
    }

    public String deleteDirector(Long id) {
        if (directorDao.checkDirectorExistInDb(id)) {
            directorDao.deleteDirector(id);
            return String.format("Режиссёр с id %s удалён", id);
        } else {
            throw new DirectorNotFoundException("Режиссёр не найден");
        }
    }

    public void validateDirectorsName(Director director) {
        if (!director.getName().isBlank()) {
            log.info("Проверка режиссёра пройдена");
        } else {
            throw new ValidationException("Ощибка в процессе валидации.");
        }
    }
}
