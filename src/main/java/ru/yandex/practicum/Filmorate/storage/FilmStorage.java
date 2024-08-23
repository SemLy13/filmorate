package ru.yandex.practicum.Filmorate.storage;

import model.Film;
import model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);
    Optional<Film> updateFilm(String id, Film filmDetails);

    List<Film> getAllFilms();

    Optional<Film> findFilmById(String id);

    void deleteFilm(String filmId);
}
