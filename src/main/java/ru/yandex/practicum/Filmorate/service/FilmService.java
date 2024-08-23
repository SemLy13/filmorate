package ru.yandex.practicum.Filmorate.service;

import lombok.extern.slf4j.Slf4j;
import model.Film;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(String filmId, String userId) {
        Optional<Film> filmOpt = filmStorage.findFilmById(filmId);
        Optional<User> userOpt = userStorage.findUserById(userId);

        if (filmOpt.isPresent() && userOpt.isPresent()) {
            Film film = filmOpt.get();

            // Проверяем, есть ли уже лайк от этого пользователя
            if (!film.getLikes().contains(userId)) {
                film.addLike(userId); // Добавляем лайк
                filmStorage.updateFilm(filmId, film); // Обновляем информацию о фильме
                log.info("User {} liked film {}.", userId, filmId);
            } else {
                log.info("User {} has already liked film {}.", userId, filmId);
            }
        } else {
            if (!filmOpt.isPresent()) {
                log.warn("Film with id {} not found.", filmId);
            }
            if (!userOpt.isPresent()) {
                log.warn("User with id {} not found.", userId);
            }
        }
    }

    public void removeLike(String filmId, String userId) {
        Optional<Film> filmOpt = filmStorage.findFilmById(filmId);

        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();

            if (film.getLikes().contains(userId)) {
                film.removeLike(userId); // Удаление лайка от пользователя
                filmStorage.updateFilm(filmId, film); // Обновляем фильм
                log.info("User {} removed like from film {}.", userId, filmId);
            } else {
                log.info("User {} has not liked film {}.", userId, filmId);
            }
        } else {
            log.warn("Film with id {} not found.", filmId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAllFilms();

        // Сортируем фильмы по количеству лайков в порядке убывания
        List<Film> sortedFilms = films.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

        log.info("Returning top {} popular films.", count);
        return sortedFilms;
    }
}
