package com.practice.ecommerce.initializer;

import com.practice.ecommerce.entity.Role;
import com.practice.ecommerce.entity.User;
import com.practice.ecommerce.exception.GlobalExceptionHandler;
import com.practice.ecommerce.repository.RoleRepository;
import com.practice.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${user.username}")
    private String userUsername;
    @Value("${user.password}")
    private String userPassword;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER", null)));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN", null)));

            if (!userRepository.findByUsername("admin").isPresent()) {
                User admin = new User();
                admin.setName("admin");
                admin.setEmail(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword)); // hashed password
                admin.setRoles(Set.of(adminRole, userRole));
                userRepository.save(admin);
                logger.info("ADMIN USER CREATE: {}", admin);

                User user = new User();
                user.setName("testUser");
                user.setEmail(userUsername);
                user.setPassword(passwordEncoder.encode(userPassword)); // hashed password
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
                logger.info("USER CREATE: {}", user);
            }
        }
    }
}
