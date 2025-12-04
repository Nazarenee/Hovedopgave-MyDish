package com.example.demo.services;

import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getProfileImage() != null && !userDTO.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(userDTO.getProfileImage());
        }

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    public UserDTO login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUserName(),
                        userDTO.getPassword()
                )
        );

        User user = userRepository.findByUserName(userDTO.getUserName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String token = jwtUtil.generateToken(user, user.getUserId());

        UserDTO response = UserMapper.toDto(user);
        response.setToken(token);

        return response;
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO, User currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean usernameChanged = false;

        if (userDTO.getUserName() != null && !userDTO.getUserName().trim().isEmpty()) {
            if (!user.getUserName().equals(userDTO.getUserName())) {
                if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
                }
                user.setUserName(userDTO.getUserName());
                usernameChanged = true;
            }
        }

        if (userDTO.getProfileImage() != null && !userDTO.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(userDTO.getProfileImage());
        }

        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().trim().isEmpty()) {
            if (userDTO.getCurrentPassword() == null || userDTO.getCurrentPassword().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
            }

            if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        User savedUser = userRepository.save(user);
        UserDTO response = UserMapper.toDto(savedUser);

        if (usernameChanged) {
            String newToken = jwtUtil.generateToken(savedUser, savedUser.getUserId());
            response.setToken(newToken);
        }
        return response;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}