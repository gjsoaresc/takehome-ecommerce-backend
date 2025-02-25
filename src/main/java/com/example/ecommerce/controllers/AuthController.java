package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.AuthRequestDTO;
import com.example.ecommerce.dto.AuthResponseDTO;
import com.example.ecommerce.dto.RegisterRequestDTO;
import com.example.ecommerce.exceptions.auth.UserAlreadyExistsException;
import com.example.ecommerce.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new user", description = "Creates a new user with a specified role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            String token = authService.registerUser(registerRequest);
            return ResponseEntity.status(201).body(new AuthResponseDTO(token));
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponseDTO("User already exists"));
        }
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            String token = authService.authenticateUser(authRequest);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDTO("Invalid credentials"));
        }
    }
}