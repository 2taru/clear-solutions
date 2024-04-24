package com.clearsolutions.repository;

import com.clearsolutions.models.UserEntity;
import com.clearsolutions.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private UserEntity user;

    @BeforeEach
    public void init() {
        user = UserEntity.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
    }

    @Test
    public void UserRepository_SaveUser_ReturnsUser() {

        UserEntity savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertSame(user, savedUser);
    }

    @Test
    public void UserRepository_FindAll_ReturnsMoreThanOneUser() {

        UserEntity user2 = UserEntity.builder().email("test2@mail.com")
                .firstName("Test2")
                .lastName("Test2")
                .birthDate(LocalDateTime.now())
                .address("Test2 st")
                .phoneNumber("380999999999")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<UserEntity> userList = userRepository.findAll();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());
        Assertions.assertTrue(userList.contains(user));
        Assertions.assertTrue(userList.contains(user2));
    }

    @Test
    public void UserRepository_FindById_ReturnsUser() {

        userRepository.save(user);

        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        Assertions.assertTrue(returnedUser.isPresent());
        Assertions.assertSame(user, returnedUser.get());
    }

    @Test
    public void UserRepository_UpdateById_ReturnsUser() {

        userRepository.save(user);

        user.setFirstName("updated");
        userRepository.save(user);
        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        Assertions.assertTrue(returnedUser.isPresent());
        Assertions.assertSame(user, returnedUser.get());
    }

    @Test
    public void UserRepository_DeleteById() {

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<UserEntity> returnedUser = userRepository.findById(user.getId());

        Assertions.assertFalse(returnedUser.isPresent());
    }

    @Test
    public void UserRepository_FindByBirthDateBetween_ReturnsListOfUsersIfExist() {

        UserEntity user2 = UserEntity.builder().email("test2@mail.com")
                .firstName("Test2")
                .lastName("Test2")
                .birthDate(LocalDateTime.of(2002, 2, 2, 0, 0, 0))
                .address("Test2 st")
                .phoneNumber("380999999999")
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        List<UserEntity> userList = userRepository.findByBirthDateBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                LocalDateTime.of(2001, 1, 2, 0, 0, 0)
        );

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(1, userList.size());
        Assertions.assertTrue(userList.contains(user));
    }
}