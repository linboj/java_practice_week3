package com.practice.ecommerce.filter;

import com.practice.ecommerce.entity.User;
import com.practice.ecommerce.service.JwtService;
import com.practice.ecommerce.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void doFilterInternal_shouldAuthenticateRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");
        when(jwtService.extractUsername(anyString())).thenReturn("testuser@test.com");
        when(jwtService.validateToken(anyString(), any())).thenReturn(true);
        when(userService.loadUserByUsername("testuser@test.com"))
                .thenReturn(new User(1L, "testuser", "testuser@test.com", "password", true, true, true, true, Collections.emptyList(), new HashSet<>()));

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
