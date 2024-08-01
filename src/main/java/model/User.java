package model;

import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @NonNull
    private String email;

    @NonNull
    private String login;

    @NonNull
    private String id;

    private String name;
    private LocalDateTime birthday;

    public User(@NonNull String email, @NonNull String login, LocalDateTime birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        id = ID();
        validate();
    }
    public static String ID(){
        return UUID.randomUUID().toString();
    }
    public void validate() {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new ValidationException("Почта не должна быть пустой и должна содержать символ '@'");
        }
        if (login == null || login.isEmpty() || login.contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым и не должен содержать пробел");
        }
        if (birthday != null && birthday.isAfter(LocalDateTime.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static void main(String[] args) {
        // Пример правильного пользователя
        try {
            User user1 = new User("user@example.com", "exampleUser", LocalDateTime.of(1990, 1, 1, 0, 0));
            System.out.println("Пользователь прошел валидацию: " + user1);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример пользователя с некорректной почтой
        try {
            User user2 = new User("userexample.com", "exampleUser", LocalDateTime.of(1990, 1, 1, 0, 0));
            System.out.println("Пользователь прошел валидацию: " + user2);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример пользователя с некорректным логином
        try {
            User user3 = new User("user@example.com", "example User", LocalDateTime.of(1990, 1, 1, 0, 0));
            System.out.println("Пользователь прошел валидацию: " + user3);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        // Пример пользователя с датой рождения в будущем
        try {
            User user4 = new User("user@example.com", "exampleUser", LocalDateTime.of(3000, 1, 1, 0, 0));
            System.out.println("Пользователь прошел валидацию: " + user4);
        } catch (ValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }
    }
}
