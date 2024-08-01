package ru.yandex.practicum.Filmorate;

import model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exceptions.ValidationException;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            film.setId(Film.ID()); // Генерация ID
            film.validate();       // Валидация
            films.add(film);
            log.info("User added successfully: {}", film);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable String id, @RequestBody Film filmDetails) {
        try {
            filmDetails.validate();
            Optional<Film> optionalFilm = films.stream()
                    .filter(film -> film.getId().equals(id))
                    .findFirst();

            if (optionalFilm.isPresent()) {
                Film film = optionalFilm.get();
                film.setName(filmDetails.getName());
                film.setReleaseDate(filmDetails.getReleaseDate());
                film.setDuration(filmDetails.getDuration());
                film.setDescription(filmDetails.getDescription());
                log.info("User updated successfully: {}", film);
                return new ResponseEntity<>(film, HttpStatus.OK);
            } else {
                log.warn("User not found with id: {}", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (ValidationException e) {
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Fetching all users");
        return new ResponseEntity<>(films, HttpStatus.OK);
    }
}
