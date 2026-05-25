package com.eduardo.expense_tracker.resource;

import com.eduardo.expense_tracker.entities.User;
import com.eduardo.expense_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> findById(Long id){
        return ResponseEntity.ok().body(userService.userFindById(id));
    }
    @GetMapping(value = "/insert")
    public ResponseEntity<User> insertUser(User user) {
        return ResponseEntity.ok().body(userService.insertUser(user));
    }
    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(value = "/update/{id}")
    public ResponseEntity<User> updateUser(Long id, User user) {
        return ResponseEntity.ok().body(userService.updateUser(id, user));
    }
}
