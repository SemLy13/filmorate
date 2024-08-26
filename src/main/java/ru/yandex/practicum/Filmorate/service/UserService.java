package ru.yandex.practicum.Filmorate.service;

import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    public void sendFriendRequest(String senderId, String receiverId) {
        User sender = userStorage.findUserById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userStorage.findUserById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        if (!sender.isFriend(receiverId) && !receiver.getFriendRequests().contains(senderId)) {
            receiver.addFriendRequest(senderId);
            userStorage.updateUser(receiverId, receiver);
            log.info("User {} sent a friend request to {}", senderId, receiverId);
        } else {
            log.warn("Friend request already exists or users are already friends.");
        }
    }
    public void confirmFriendship(String receiverId, String senderId) {
        User receiver = userStorage.findUserById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        User sender = userStorage.findUserById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        // Проверяем, что у получателя есть запрос дружбы от отправителя
        if (receiver.getFriendRequests().contains(senderId)) {
            // Добавляем каждого пользователя в список друзей другого
            receiver.getFriends().add(senderId);
            sender.getFriends().add(receiverId);

            // Убираем запрос на дружбу из списка запросов
            receiver.getFriendRequests().remove(senderId);

            // Обновляем пользователей в хранилище
            userStorage.updateUser(receiverId, receiver);
            userStorage.updateUser(senderId, sender);

            log.info("User {} confirmed friendship with {}", receiverId, senderId);
        } else {
            log.warn("No friend request from user {} to {}", receiverId);
        }
    }


    public void removeFriend(String userId, String friendId) {
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User friend = userStorage.findUserById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not found"));

        if (user.isFriend(friendId)) {
            user.removeFriend(friendId);
            friend.removeFriend(userId);

            userStorage.updateUser(userId, user);
            userStorage.updateUser(friendId, friend);

            log.info("Users {} and {} are no longer friends.", userId, friendId);
        } else {
            log.warn("Users {} and {} are not friends.", userId, friendId);
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
