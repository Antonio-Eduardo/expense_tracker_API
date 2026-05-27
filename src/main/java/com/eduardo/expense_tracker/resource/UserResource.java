package com.eduardo.expense_tracker.resource;

import com.eduardo.expense_tracker.dtos.UserDTO;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok().body(userService.userFindAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.userFindById(id));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        return ResponseEntity.ok().body(userService.updateUser(id, user));
    }
}
