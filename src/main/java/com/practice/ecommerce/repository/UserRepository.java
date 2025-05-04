package com.practice.ecommerce.repository;

import com.practice.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername (String username);
    User findUserByEmail (String email);
}
