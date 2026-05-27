package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.AuthenticationDTO;
import com.eduardo.expense_tracker.dtos.LoginResponseDTO;
import com.eduardo.expense_tracker.dtos.RegisterDTO;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.infra.TokenService;
import com.eduardo.expense_tracker.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if (this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().body("Email already in use");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.email(), encryptedPassword, data.role());

        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
