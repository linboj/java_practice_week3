package com.practice.ecommerce.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.dto.UnifiedAPIResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        logger.warn("Access denied: {}", accessDeniedException.getMessage());

        ErrorDetails error = new ErrorDetails(
                HttpStatus.FORBIDDEN,
                "Access denied: " + accessDeniedException.getMessage(),
                request
        );

        UnifiedAPIResponse<Void> unifiedAPIResponse = new UnifiedAPIResponse<>(false, null, error);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        mapper.writeValue(response.getOutputStream(), unifiedAPIResponse);
    }
}
