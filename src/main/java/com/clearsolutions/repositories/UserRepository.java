package com.clearsolutions.repositories;

import com.clearsolutions.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findByBirthDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
