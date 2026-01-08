package org.example.finalproject.controllers;


import org.example.finalproject.dto.AuthResponseDTO;
import org.example.finalproject.dto.LoginDTO;
import org.example.finalproject.dto.UserDTO;

import org.example.finalproject.entity.User;
import org.example.finalproject.repository.UserRepository;
import org.example.finalproject.services.UserService;
import org.example.finalproject.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name = "Users", description = "API для управления пользователями")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/secret")
    public String getUsers() {
        return "mircefer bagirov";
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/all-users")
    public List<UserDTO> getall() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public UserDTO createUser(UserDTO user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id,
                              @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return userService.updateUser(userDTO);
    }
}
