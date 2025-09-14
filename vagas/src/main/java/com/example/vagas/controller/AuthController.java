package com.example.vagas.controller;

import com.example.vagas.model.User;
import com.example.vagas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = authService.registerUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        return authService.authenticateUser(user.getUsername(), user.getPassword())
                .map(authenticatedUser -> new ResponseEntity<Object>(authenticatedUser, HttpStatus.OK))
                .orElse(new ResponseEntity<Object>("Invalid credentials", HttpStatus.UNAUTHORIZED));
    }
}