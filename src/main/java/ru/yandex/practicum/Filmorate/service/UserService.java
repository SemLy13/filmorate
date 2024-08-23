package ru.yandex.practicum.Filmorate.service;

import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(String userId, String friendId) {
        Optional<User> userOpt = userStorage.findUserById(userId);
        Optional<User> friendOpt = userStorage.findUserById(friendId);

        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            user.addFriend(friendId);
            friend.addFriend(userId);

            userStorage.updateUser(userId, user);
            userStorage.updateUser(friendId, friend);

            log.info("User {} and {} are now friends.", userId, friendId);
        } else {
            if (!userOpt.isPresent()) {
                log.warn("User with id {} not found.", userId);
            }
            if (!friendOpt.isPresent()) {
                log.warn("User with id {} not found.", friendId);
            }
        }
    }

    // Удаление из друзей
    public void removeFriend(String userId, String friendId) {
        Optional<User> userOpt = userStorage.findUserById(userId);
        Optional<User> friendOpt = userStorage.findUserById(friendId);

        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            user.removeFriend(friendId);
            friend.removeFriend(userId);

            userStorage.updateUser(userId, user);
            userStorage.updateUser(friendId, friend);

            log.info("User {} and {} are no longer friends.", userId, friendId);
        } else {
            if (!userOpt.isPresent()) {
                log.warn("User with id {} not found.", userId);
            }
            if (!friendOpt.isPresent()) {
                log.warn("User with id {} not found.", friendId);
            }
        }
    }

    // Получение списка друзей пользователя
    public Set<User> getFriends(String userId) {
        Optional<User> userOpt = userStorage.findUserById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<String> friendIds = user.getFriends();
            Set<User> friends = new HashSet<>();

            for (String friendId : friendIds) {
                userStorage.findUserById(friendId).ifPresent(friends::add);
            }

            log.info("Found {} friends for user {}.", friends.size(), userId);
            return friends;
        } else {
            log.warn("User with id {} not found.", userId);
            return new HashSet<>();
        }
    }

    // Получение списка общих друзей
    public Set<User> getCommonFriends(String userId, String otherId) {
        Set<User> commonFriends = new HashSet<>();

        Set<User> userFriends = getFriends(userId);
        Set<User> otherUserFriends = getFriends(otherId);

        for (User friend : userFriends) {
            if (otherUserFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }

        log.info("Found {} common friends between user {} and {}.", commonFriends.size(), userId, otherId);
        return commonFriends;
    }
}
