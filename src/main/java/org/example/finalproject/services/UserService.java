package org.example.finalproject.services;


import org.example.finalproject.dto.UserDTO;
import org.example.finalproject.entity.User;
import org.example.finalproject.mapper.UserMapper;
import org.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public UserDTO createUser(UserDTO userdto) {
        if (userRepository.existsByEmail(userdto.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }
        User user = userMapper.toEntity(userdto);

        //user.password = passwordEncoder.encode(user.password);
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);

    }


    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDTO(user);

    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)  // преобразуем User → UserDTO
                .toList();
    }

    public UserDTO updateUser(UserDTO userdto) {
        User user = userRepository.findById(userdto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userdto.getName());
        user.setEmail(userdto.getEmail());
        user.setPhone(userdto.getPhone());

        if (userdto.getPassword() != null && !userdto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        }

        return userMapper.toDTO(userRepository.save(user));
    }
}
