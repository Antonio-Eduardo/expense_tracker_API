package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.UserDTO;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users",description = "Operações relacionadas aos Usuários")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok().body(userService.userFindAll());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar usuário por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.userFindById(id));
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Deletar um usuário por Id")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Atualizar um usuário pelo Id")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        return ResponseEntity.ok().body(userService.updateUser(id, user));
    }
}
