package ru.yandex.practicum.Filmorate;

import model.Film;
import ru.yandex.practicum.Filmorate.service.FilmService;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.Filmorate.exceptions.ValidationException;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable String id, @PathVariable String userId) {
        filmService.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/like/{friendId}")
    public ResponseEntity<Void> removeLike(@PathVariable String id, @PathVariable String userId) {
        filmService.removeLike(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> popularFilms = filmService.getPopularFilms(count);
        return new ResponseEntity<>(popularFilms, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            film.validate();  // Валидация
            filmStorage.addFilm(film);
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
            filmDetails.validate();  // Валидация
            Optional<Film> updatedFilm = filmStorage.updateFilm(id, filmDetails);

            if (updatedFilm.isPresent()) {
                log.info("Film updated successfully: {}", updatedFilm.get());
                return new ResponseEntity<>(updatedFilm.get(), HttpStatus.OK);
            } else {
                log.warn("Film not found with id: {}", id);
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
        return new ResponseEntity<>(filmStorage.getAllFilms(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable String id) {
        Optional<Film> film = filmStorage.findFilmById(id);

        if (film.isPresent()) {
            filmStorage.deleteFilm(id);
            log.info("Film deleted successfully with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("Film not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
