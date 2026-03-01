package com.application.portal.Controller;

import com.application.portal.Dto.AuthRequestDTO;
import com.application.portal.Dto.AuthResponseDTO;
import com.application.portal.Exception.InvalidCredentialsException;
import com.application.portal.Exception.UserAlreadyExistException;
import com.application.portal.Exception.UsernameAlreadyExists;
import com.application.portal.Model.Role;
import com.application.portal.Model.User;
import com.application.portal.Repository.UserRepository;
import com.application.portal.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private UserRepository repo;

    private PasswordEncoder encoder;

    private AuthenticationManager authManager;

    private  JwtService jwtService;

    @Autowired
    public AuthController(UserRepository repo,
                          PasswordEncoder encoder,
                          AuthenticationManager authManager,
                          JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO request) {

        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExists("user already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        // Set role correctly
        String role = "ROLE_" + request.getRole();
        user.setRole(Role.valueOf(role));

        repo.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .role(String.valueOf(user.getRole()))
                .username(user.getUsername())
                .build());
    }

}
