package model;

import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.yandex.practicum.Filmorate.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    public Set<String> friends = new HashSet<>();

    private Set<String> friendRequests = new HashSet<>();

    public User(@NonNull String email, @NonNull String login, LocalDateTime birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        id = ID();
        validate();
    }
    public User(@NonNull String email, @NonNull String login, LocalDateTime birthday, String id) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.id = id;
        validate();
    }
    public void addFriendRequest(String userId) {
        friendRequests.add(userId);
    }
    public boolean isFriend(String userId) {
        return friends.contains(userId);
    }
    public void confirmFriend(String userId) {
        if (friendRequests.remove(userId)) {
            friends.add(userId);
        }
    }
    public static String ID(){
        return UUID.randomUUID().toString();
    }
    public void validate() {
        if (id == null) {id = ID();}
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
    public void addFriend(String friendId) {
        friends.add(friendId);
    }

    public void removeFriend(String friendId) {
        friends.remove(friendId);
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
