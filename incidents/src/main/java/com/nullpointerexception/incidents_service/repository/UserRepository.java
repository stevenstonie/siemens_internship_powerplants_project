package com.nullpointerexception.incidents_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nullpointerexception.incidents_service.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
