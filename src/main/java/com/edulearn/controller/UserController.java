package com.edulearn.controller;

import com.edulearn.model.User;
import com.edulearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search
    ) {
        List<User> users;

        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search);
        } else if (role != null && !role.isEmpty()) {
            users = userService.findByRole(role);
        } else {
            users = userService.findAll();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return (ResponseEntity<User>) userService.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    try {
                        return ResponseEntity.ok(userService.updateUser(user));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.<User>badRequest().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        try {
            userService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        long totalUsers = userService.findAll().size();
        long instructors = userService.countByRole("instructor");
        long students = userService.countByRole("student");

        Map<String, Object> stats = Map.of(
                "totalUsers", totalUsers,
                "instructors", instructors,
                "students", students
        );

        return ResponseEntity.ok(stats);
    }
}