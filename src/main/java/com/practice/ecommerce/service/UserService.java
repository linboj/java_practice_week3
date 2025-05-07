package com.practice.ecommerce.service;

import com.practice.ecommerce.entity.User;
import com.practice.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // use email as username
            User user = userRepository.findUserByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("使用者不存在");
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
