package ru.yandex.practicum.Filmorate.storage;

import model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    void addUser(User user);

    Optional<User> updateUser(String id, User userDetails);

    List<User> getAllUsers();

    Optional<User> findUserById(String id);

    void deleteUser(String userId);
}
