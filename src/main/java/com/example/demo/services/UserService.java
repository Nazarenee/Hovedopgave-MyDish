package com.example.demo.services;

import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users =  userRepository.findAll();
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        User user =  userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

    public UserDTO createUser(RegisterRequest registerRequest) {
        if (userRepository.findByUserName(registerRequest.getUserName()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
