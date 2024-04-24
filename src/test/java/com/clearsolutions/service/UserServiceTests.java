package com.clearsolutions.service;

import com.clearsolutions.dto.UserDTO;
import com.clearsolutions.exception.UserNotFoundException;
import com.clearsolutions.mapper.UserMapper;
import com.clearsolutions.models.UserEntity;
import com.clearsolutions.repositories.UserRepository;
import com.clearsolutions.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.AccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;
    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
        userDTO = UserMapper.mapToDto(user);
        userService.requiredAge = 18;
    }

    @Test
    public void UserService_Create_ReturnsUserDto() throws AccessException {

        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserDTO savedUserDto = userService.createUser(userDTO);

        assertThat(savedUserDto).isNotNull();
        assertThat(savedUserDto).isEqualTo(userDTO);
    }

    @Test
    public void UserService_FindById_ReturnsUserDto() {

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        UserDTO returnedUserDto = userService.getUserById(1);

        assertThat(returnedUserDto).isNotNull();
        assertThat(returnedUserDto).isEqualTo(userDTO);
    }

    @Test
    public void UserService_FindAll_ReturnsListOfUserDto() {

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> returnedUserDTOs = userService.getAllUsers();

        assertThat(returnedUserDTOs).isNotNull();
        assertThat(returnedUserDTOs.size()).isEqualTo(1);
        assertThat(returnedUserDTOs.contains(userDTO)).isTrue();
    }

    @Test
    public void UserService_FindByBirthDateBetween_ReturnsListOfUsersIfExist() {

        when(userRepository.findByBirthDateBetween(Mockito.any(), Mockito.any())).thenReturn(List.of(user));

        List<UserDTO> returnedUserDTOs = userService.getUsersByBirthDateRange(LocalDateTime.now(), LocalDateTime.now());

        assertThat(returnedUserDTOs).isNotNull();
        assertThat(returnedUserDTOs.size()).isEqualTo(1);
        assertThat(returnedUserDTOs.contains(userDTO)).isTrue();
    }

    @Test
    public void UserService_UpdateById_ReturnsUserDto() {

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserDTO updatedUserDto = userService.updateUserById(1, userDTO);

        assertThat(updatedUserDto).isNotNull();
        assertThat(updatedUserDto).isEqualTo(userDTO);
    }

    @Test
    public void UserService_DeleteById() {

        assertAll(() -> userService.deleteUserById(1));
    }

    @Test
    public void UserService_CreateUser_ThrowsIllegalArgumentException() {

        UserDTO tempUserDTO = UserMapper.mapToDto(user);
        tempUserDTO.setBirthDate(LocalDateTime.now());

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(tempUserDTO));
    }

    @Test
    public void UserService_UpdateById_ThrowsIllegalArgumentException() {

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        UserDTO tempUserDTO = UserMapper.mapToDto(user);
        tempUserDTO.setBirthDate(LocalDateTime.now().minusYears(17));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUserById(1, tempUserDTO));
    }

    @Test
    public void UserService_UserNotFoundException_ThrowsUserNotFoundException() {

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserById(1, new UserDTO()));
    }

    @Test
    public void UserService_GetUsersByBirthDateRange_ThrowsIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUsersByBirthDateRange(
                LocalDateTime.of(2002, 2, 2, 0, 0, 0),
                LocalDateTime.of(2001, 1, 1, 0, 0, 0)
        ));
    }
}