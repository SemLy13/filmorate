package ru.yandex.practicum.Filmorate.storage;

import model.Film;
import model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final List<Film> films = new ArrayList<>();

    @Override
    public void addFilm(Film film) {
        films.add(film);
    }
    @Override
    public Optional<Film> updateFilm(String id, Film filmDetails) {
        Optional<Film> optionalUser = films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();

        if (optionalUser.isPresent()) {
            Film film = optionalUser.get();
            film.setName(filmDetails.getName());
            film.setReleaseDate(film.getReleaseDate());
            film.setDuration(filmDetails.getDuration());
            film.setDescription(filmDetails.getDescription());
            return Optional.of(film);
        }
        return Optional.empty();
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films);
    }

    public Optional<Film> findFilmById(String id) {
        return films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteFilm(String filmId) {
        films.removeIf(film -> film.getId().equals(filmId));
    }

}
