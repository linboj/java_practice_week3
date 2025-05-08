package com.practice.ecommerce.controller;

import com.practice.ecommerce.dto.AuthRequest;
import com.practice.ecommerce.dto.AuthResponse;
import com.practice.ecommerce.dto.UnifiedAPIResponse;
import com.practice.ecommerce.entity.User;
import com.practice.ecommerce.service.JwtService;
import com.practice.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Login.",
            description = "User login.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User login successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Username or password error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) throws Exception {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        } catch (Exception e) {
            throw new Exception("Error", e);
        }

        final UserDetails user = userService
                .loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @DeleteMapping("/logout")
    @Operation(
            summary = "Logout.",
            description = "User logout.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logout successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok("Logged out successfully.");
    }
}
