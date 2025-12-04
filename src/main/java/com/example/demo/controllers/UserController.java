package com.example.demo.controllers;

import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @Validated(UserDTO.OnCreate.class) @RequestBody UserDTO userDTO) {
        UserDTO user = userService.register(userDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody UserDTO userDTO) {
        UserDTO response = userService.login(userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!currentUser.getUserId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update other users' profiles");
        }

        UserDTO updatedUser = userService.updateUser(id, userDTO, currentUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}