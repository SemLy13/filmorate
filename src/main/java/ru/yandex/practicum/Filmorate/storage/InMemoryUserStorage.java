package ru.yandex.practicum.Filmorate.storage;

import model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        //user.setId(User.ID()); // Генерация ID
        users.add(user);
    }

    @Override
    public Optional<User> updateUser(String id, User userDetails) {
        Optional<User> optionalUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setLogin(userDetails.getLogin());
            user.setBirthday(userDetails.getBirthday());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> findUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteUser(String userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }
}
