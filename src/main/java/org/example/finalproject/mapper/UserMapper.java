package org.example.finalproject.mapper;

import org.example.finalproject.dto.UserDTO;
import org.example.finalproject.entity.User;
import org.hibernate.annotations.CollectionTypeRegistration;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.id);
        dto.setName(user.name);
        dto.setEmail(user.email);
        dto.setRole(user.role);
        dto.setPhone(user.phone);
        dto.setPhoto(user.photo);
        //
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
       // user.id = dto.getId();
        user.name = dto.getName();
        user.email = dto.getEmail();
       // user.password = dto.getPassword();
        user.role = dto.getRole();
        user.phone = dto.getPhone();
        user.photo = dto.getPhoto();

        return user;
    }
}
