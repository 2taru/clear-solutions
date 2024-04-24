
package com.clearsolutions.controllers;

import com.clearsolutions.dto.ResponseDTO;
import com.clearsolutions.dto.UserDTO;
import com.clearsolutions.services.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {

        userService.createUser(userDTO);

        return new ResponseEntity<>("User registered!", HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable("id") int userId) {

        UserDTO data = userService.getUserById(userId);
        ResponseDTO response = ResponseDTO.builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllUsers() {

        List<UserDTO> data = userService.getAllUsers();
        ResponseDTO response = ResponseDTO.builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/usersByBirthDateRange")
    public ResponseEntity<ResponseDTO> getUsersByBirthDateRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {

        List<UserDTO> data = userService.getUsersByBirthDateRange(fromDate, toDate);
        ResponseDTO response = ResponseDTO.builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ResponseDTO> updateUserById(@RequestBody UserDTO userDTO, @PathVariable("id") int userId) {

        UserDTO data = userService.updateUserById(userId, userDTO);
        ResponseDTO response = ResponseDTO.builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int userId) {

        userService.deleteUserById(userId);

        return new ResponseEntity<>("Deleted user with id = " + userId, HttpStatus.OK);
    }
}
