package services;

import com.clearsolutions.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO user);

    UserDTO updateUserById(int userId, UserDTO user);

    UserDTO getUserById(int userId);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByBirthDateRange(LocalDateTime fromDate, LocalDateTime toDate);

    void deleteUserById(int userId);
}
