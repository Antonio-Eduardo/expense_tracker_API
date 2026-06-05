package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.AuthenticationDTOrequest;
import com.eduardo.expense_tracker.dtos.request.LoginResponseDTOrequest;
import com.eduardo.expense_tracker.dtos.request.RegisterDTOrequest;
import com.eduardo.expense_tracker.dtos.response.LoginResponseDTOresponse;
import com.eduardo.expense_tracker.dtos.response.RegisterDTOresponse;
import com.eduardo.expense_tracker.dtos.response.UserDTOresponse;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.infra.security.TokenService;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operações relacionadas à autenticação de usuários")
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
    @Operation(summary = "Autentica um usuário e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado")
    @ApiResponse(responseCode = "400", description = "Dados de autenticação inválidos")
    public ResponseEntity<LoginResponseDTOresponse> login(@RequestBody @Valid AuthenticationDTOrequest data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTOresponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário com email, senha e função")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de registro inválidos")
    public ResponseEntity<RegisterDTOresponse> register(@RequestBody @Valid RegisterDTOrequest data){
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        RegisterDTOrequest registerDTO = new RegisterDTOrequest(data.email(), encryptedPassword, data.role());
        RegisterDTOresponse registerDTOresponse = userService.createUser(registerDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(registerDTOresponse.getId()).toUri();
        return ResponseEntity.created(uri).body(userService.createUser(registerDTO));
    }
}
