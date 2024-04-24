package com.clearsolutions.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int id;
    @NotEmpty(message = "should not be empty!")
    @Email(message = "email format is not correct!")
    private String email;
    @NotEmpty(message = "should not be empty!")
    @Size(min = 2, message = "user first name should have at least 2 characters!")
    private String firstName;
    @NotEmpty(message = "should not be empty!")
    @Size(min = 2, message = "user last name should have at least 2 characters!")
    private String lastName;
    @Past(message = "must contain a past date!")
    @NotNull(message = "should not be empty!")
    private LocalDateTime birthDate;
    private String address;
    private String phoneNumber;
}
