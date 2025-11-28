package com.example.demo.controllers;

import com.example.demo.DTO.AuthResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO createdUser = userService.createUser(registerRequest);
        UserDetails userDetails = userDetailsService.loadUserByUsername(createdUser.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token, createdUser.getUserName(), createdUser.getUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new AuthResponse(token, loginRequest.getUserName(), user.getUserId()));
    }
}