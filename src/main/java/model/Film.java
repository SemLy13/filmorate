package model;

import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.yandex.practicum.Filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    @NonNull
    private String name;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    private long duration;

    @NonNull
    private String id;

    private String description;

    public Set<String> likes = new HashSet<>();
    public enum Genre {
        COMEDY,
        DRAMA,
        ANIMATION,
        THRILLER,
        DOCUMENTARY,
        ACTION
    }
    public enum MPA {
        G,        // General Audience
        PG,       // Parental Guidance Suggested
        PG_13,    // Parents Strongly Cautioned
        R,        // Restricted
        NC_17     // Adults Only
    }
    private Genre genre;
    private MPA mpa;

    public Film(@NonNull String name, @NonNull LocalDate releaseDate, @NonNull long duration, Genre genre, MPA mpa) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        id = ID();
        this.genre = genre;
        this.mpa = mpa;
        validate();
    }
    public Film(@NonNull String name, @NonNull LocalDate releaseDate, @NonNull long duration, String id, Genre genre, MPA mpa) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
        this.genre = genre;
        this.mpa = mpa;
        validate();
    }
    public Film(@NonNull String name, @NonNull LocalDate releaseDate, @NonNull long duration, String id) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
        validate();
    }
    public Film(@NonNull String name, @NonNull LocalDate releaseDate, @NonNull long duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        id = ID();
        validate();
    }
    public static String ID(){
        return UUID.randomUUID().toString();
    }
    public void validate() {
        if (id == null) {id = ID();}
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Название не должно быть пустым");
        }
        if (name.length() > 200) {
            throw new ValidationException("Максимальная длина названия - 200 символов");
        }
        if (releaseDate == null) {
            throw new ValidationException("Дата релиза не должна быть пустой");
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (duration <= 0) {
            throw new ValidationException("Продолжительность должна быть больше 0");
        }
    }
    public void addLike(String likeId){
        likes.add(likeId);
    }
    public void removeLike(String likeId){
        likes.remove(likeId);
    }
    public static void main(String[] args) {
        // Пример правильного фильма
        try {
            Film film1 = new Film("Example Film", LocalDate.of(2020, 1, 1), 120);
            System.out.println("Фильм прошел валидацию: " + film1);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример фильма с пустым названием
        try {
            Film film2 = new Film("", LocalDate.of(2020, 1, 1), 120);
            System.out.println("Фильм прошел валидацию: " + film2);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример фильма с длинным названием
        try {
            Film film3 = new Film("A".repeat(201), LocalDate.of(2020, 1, 1), 120);
            System.out.println("Фильм прошел валидацию: " + film3);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример фильма с датой релиза раньше 28 декабря 1895 года
        try {
            Film film4 = new Film("Old Film", LocalDate.of(1800, 1, 1), 120);
            System.out.println("Фильм прошел валидацию: " + film4);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример фильма с отрицательной продолжительностью
        try {
            Film film5 = new Film("Negative Duration Film", LocalDate.of(2020, 1, 1), -10);
            System.out.println("Фильм прошел валидацию: " + film5);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }
    }
}
