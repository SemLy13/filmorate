package ru.yandex.practicum.Filmorate;

import model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.yandex.practicum.Filmorate.service.UserService;
import ru.yandex.practicum.Filmorate.exceptions.ValidationException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable String id, @PathVariable String friendId) {
        userService.sendFriendRequest(id, friendId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/friends/{friendId}/confirm")
    public ResponseEntity<Void> confirmFriendship(@PathVariable String id, @PathVariable String friendId) {
        userService.confirmFriendship(id, friendId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable String id, @PathVariable String friendId) {
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<User>> getAllFriends(@PathVariable String id) {
        Set<User> friends = userService.getFriends(id);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<User>> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        Set<User> commonFriends = userService.getCommonFriends(id, otherId);
        return new ResponseEntity<>(commonFriends, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            user.validate();  // Валидация
            userStorage.addUser(user);
            log.info("User added successfully: {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        try {
            userDetails.validate();  // Валидация
            Optional<User> updatedUser = userStorage.updateUser(id, userDetails);

            if (updatedUser.isPresent()) {
                log.info("User updated successfully: {}", updatedUser.get());
                return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
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
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        return new ResponseEntity<>(userStorage.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        Optional<User> user = userStorage.findUserById(id);

        if (user.isPresent()) {
            userStorage.deleteUser(id);
            log.info("User deleted successfully with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("User not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
