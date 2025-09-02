package com.garv.task_api;

import com.garv.task_api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtUtil jwtUtil;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account with username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    public ResponseEntity<String> register(@RequestBody User user) {
        // Check if user already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and receive JWT token for API access")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser.isPresent() && passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}