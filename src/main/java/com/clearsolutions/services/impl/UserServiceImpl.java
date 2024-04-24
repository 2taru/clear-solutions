package com.clearsolutions.services.impl;

import com.clearsolutions.dto.UserDTO;
import com.clearsolutions.exception.UserNotFoundException;
import com.clearsolutions.mapper.UserMapper;
import com.clearsolutions.models.UserEntity;
import com.clearsolutions.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.clearsolutions.services.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Value("${user.required-age}")
    // public only for testing purpose
    public int requiredAge;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        UserEntity user = UserMapper.mapToEntity(userDTO);

        if (ChronoUnit.YEARS.between(user.getBirthDate(), LocalDateTime.now()) < requiredAge) {
            throw new IllegalArgumentException("Age should be more than or equal to " + requiredAge + '!');
        }

        user = userRepository.save(user);

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserDTO updateUserById(int userId, UserDTO userDTO) {

        UserEntity tempUser = new UserEntity();
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + " - not found!"));

        BeanUtils.copyProperties(userDTO, tempUser, "id", "birthDate");
        if (userDTO.getBirthDate() != null) {
            if (ChronoUnit.YEARS.between(userDTO.getBirthDate(), LocalDateTime.now()) < requiredAge) {
                throw new IllegalArgumentException("Age should be more than or equal to " + 18 + '!');
            }
            tempUser.setBirthDate(userDTO.getBirthDate());
        }
        BeanUtils.copyProperties(tempUser, existingUser, "id");

        UserEntity updatedUser = userRepository.save(existingUser);

        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    public UserDTO getUserById(int userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + " - not found!"));

        return UserMapper.mapToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll().stream()
                .map(UserMapper::mapToDto)
                .toList();
    }

    @Override
    public List<UserDTO> getUsersByBirthDateRange(LocalDateTime fromDate, LocalDateTime toDate) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("'fromDate' must be before the 'toDate'!");
        }

        return userRepository.findByBirthDateBetween(fromDate, toDate).stream()
                .map(UserMapper::mapToDto)
                .toList();
    }


    @Override
    public void deleteUserById(int userId) {

        userRepository.deleteById(userId);
    }
}
