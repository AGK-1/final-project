package org.example.finalproject.controllers;

import org.example.finalproject.dto.AuthResponseDTO;
import org.example.finalproject.dto.LoginDTO;
import org.example.finalproject.dto.UserDTO;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.UserRepository;
import org.example.finalproject.services.AuthService;
import org.example.finalproject.services.UserService;
import org.example.finalproject.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController( AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserDTO createUser(UserDTO user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

}
