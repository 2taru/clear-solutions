package com.clearsolutions.controller;

import com.clearsolutions.controllers.UserController;
import com.clearsolutions.dto.UserDTO;
import com.clearsolutions.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        userDTO = UserDTO.builder().email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDateTime.now())
                .address("Test st")
                .phoneNumber("380999999999")
                .build();
    }

    @Test
    public void UserController_Create_ReturnsString() throws Exception {

        when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void UserController_GetById_ReturnsUserDTO() throws Exception {

        when(userService.getUserById(1)).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName", CoreMatchers.is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName", CoreMatchers.is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email", CoreMatchers.is(userDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.anything()));
    }

    @Test
    public void UserController_GetAll_ReturnsListOfUserDTOs() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        ResultActions response = mockMvc.perform(get("/api/users"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_FindByBirthDateBetween_ReturnsListOfUsersIfExist() throws Exception {

        when(userService.getUsersByBirthDateRange(LocalDateTime.now(), LocalDateTime.now())).thenReturn(List.of(userDTO));

        ResultActions response = mockMvc.perform(get("/api/usersByBirthDateRange?fromDate=2023-09-20T12:58:01&toDate=2023-09-25T12:58:01"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_UpdateById_ReturnsUserDTO() throws Exception {

        when(userService.updateUserById(1, userDTO)).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName", CoreMatchers.is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName", CoreMatchers.is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email", CoreMatchers.is(userDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.anything()));
    }

    @Test
    public void UserController_DeleteById_ReturnsString() throws Exception {

        doNothing().when(userService).deleteUserById(1);

        ResultActions response = mockMvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is("Deleted user with id = 1")));
    }
}