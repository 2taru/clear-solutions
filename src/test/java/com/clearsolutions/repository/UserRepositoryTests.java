package com.clearsolutions.repository;

import com.clearsolutions.models.UserEntity;
import com.clearsolutions.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveUser_ReturnsUser() {

        // Arrange
        UserEntity user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.now())
                .address("Test st")
                .phoneNumber("380999999999")
                .build();

        // Act
        UserEntity savedUser = userRepository.save(user);

        // Assert
        Assertions.assertNotNull(savedUser);
        Assertions.assertSame(user, savedUser);
    }

    @Test
    public void UserRepository_FindAll_ReturnsMoreThanOneUser() {

        // Arrange
        UserEntity user1 = UserEntity.builder().email("test1@mail.com")
                .firstName("Test1")
                .lastName("Test1")
                .birthDate(LocalDateTime.now())
                .address("Test1 st")
                .phoneNumber("380999999999")
                .build();
        UserEntity user2 = UserEntity.builder().email("test2@mail.com")
                .firstName("Test2")
                .lastName("Test2")
                .birthDate(LocalDateTime.now())
                .address("Test2 st")
                .phoneNumber("380999999999")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<UserEntity> userList = userRepository.findAll();

        // Assert
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());
        Assertions.assertTrue(userList.contains(user1));
        Assertions.assertTrue(userList.contains(user2));
    }

    @Test
    public void UserRepository_FindById_ReturnsUser() {

        // Arrange
        UserEntity user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.now())
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
        userRepository.save(user);

        // Act
        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        // Assert
        Assertions.assertTrue(returnedUser.isPresent());
        Assertions.assertSame(user, returnedUser.get());
    }

    @Test
    public void UserRepository_UpdateById_ReturnsUser() {

        // Arrange
        UserEntity user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.now())
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
        userRepository.save(user);

        // Act
        user.setFirstName("updated");
        userRepository.save(user);
        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        // Assert
        Assertions.assertTrue(returnedUser.isPresent());
        Assertions.assertSame(user, returnedUser.get());
    }

    @Test
    public void UserRepository_DeleteById() {

        // Arrange
        UserEntity user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.now())
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
        userRepository.save(user);

        // Act
        userRepository.deleteById(user.getId());
        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        // Assert
        Assertions.assertFalse(returnedUser.isPresent());
    }

    @Test
    public void UserRepository_FindByBirthDateBetween_ReturnsListOfUsersIfExist() {

        // Arrange
        UserEntity user1 = UserEntity.builder().email("test1@mail.com")
                .firstName("Test1")
                .lastName("Test1")
                .birthDate(LocalDateTime.of(2001, 1, 1, 0, 0, 0))
                .address("Test1 st")
                .phoneNumber("380999999999")
                .build();
        UserEntity user2 = UserEntity.builder().email("test2@mail.com")
                .firstName("Test2")
                .lastName("Test2")
                .birthDate(LocalDateTime.of(2002, 2, 2, 0, 0, 0))
                .address("Test2 st")
                .phoneNumber("380999999999")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<UserEntity> userList = userRepository.findByBirthDateBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                LocalDateTime.of(2001, 1, 2, 0, 0, 0)
        );

        // Assert
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(1, userList.size());
        Assertions.assertTrue(userList.contains(user1));
    }
}