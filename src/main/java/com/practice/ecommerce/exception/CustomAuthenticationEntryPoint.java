package com.practice.ecommerce.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.dto.UnifiedAPIResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        logger.warn("Unauthorized access attempt: {}", authException.getMessage());

        ErrorDetails error = new ErrorDetails(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized: " + authException.getMessage(),
                request
        );

        UnifiedAPIResponse<Void> unifiedAPIResponse = new UnifiedAPIResponse<>(false, null, error);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(response.getOutputStream(), unifiedAPIResponse);
    }
}
