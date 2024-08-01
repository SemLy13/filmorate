package ru.yandex.practicum.Filmorate;

import model.User;
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
@RequestMapping("/user")
public class UserController {
    private List<User> users = new ArrayList<>();

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            user.setId(User.ID()); // Генерация ID
            user.validate();       // Валидация
            users.add(user);
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
            userDetails.validate();
            Optional<User> optionalUser = users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst();

            if (optionalUser.isPresent()) {
                User  user = optionalUser.get();
                user.setName( userDetails.getName());
                user.setEmail( userDetails.getEmail());
                user.setLogin( userDetails.getLogin());
                user.setBirthday( userDetails.getBirthday());
                log.info("User updated successfully: {}", user);
                return new ResponseEntity<>( user, HttpStatus.OK);
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
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
