package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.AuthenticationDTOrequest;
import com.eduardo.expense_tracker.dtos.request.LoginResponseDTOrequest;
import com.eduardo.expense_tracker.dtos.request.RegisterDTOrequest;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.infra.security.TokenService;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.UserService;
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
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTOrequest data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTOrequest(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTOrequest data){
        if (this.userRepository.findByEmail(data.email()).isPresent()) return ResponseEntity.badRequest().body("Email already in use");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        RegisterDTOrequest registerDTO = new RegisterDTOrequest(data.email(), encryptedPassword, data.role());


        return ResponseEntity.ok().body(userService.createUser(registerDTO));
    }
}
