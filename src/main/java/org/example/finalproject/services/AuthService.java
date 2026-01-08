package org.example.finalproject.services;

import org.example.finalproject.dto.AuthResponseDTO;
import org.example.finalproject.dto.LoginDTO;
import org.example.finalproject.dto.UserDTO;
import org.example.finalproject.entity.User;
import org.example.finalproject.mapper.UserMapper;
import org.example.finalproject.repository.UserRepository;
import org.example.finalproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }



    public UserDTO register(UserDTO userdto) {
        if (userRepository.existsByEmail(userdto.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }
        User user = userMapper.toEntity(userdto);

        //user.password = passwordEncoder.encode(user.password);
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }


    public AuthResponseDTO login(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }
}
