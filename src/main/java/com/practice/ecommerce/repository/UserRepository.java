package com.practice.ecommerce.repository;

import com.practice.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :username")
    User findUserByUsername (@Param("username") String username);
    @Query("SELECT u FROM User u WHERE u.email = :username")
    Optional<User> findByUsername (@Param("username") String username);
    User findUserByEmail (String email);
}
