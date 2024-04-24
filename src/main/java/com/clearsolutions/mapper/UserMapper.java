package com.clearsolutions.mapper;

import com.clearsolutions.dto.UserDTO;
import com.clearsolutions.models.UserEntity;

public class UserMapper {

    public static UserDTO mapToDto(UserEntity user) {

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static UserEntity mapToEntity(UserDTO userDTO) {

        return UserEntity.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .birthDate(userDTO.getBirthDate())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }
}
