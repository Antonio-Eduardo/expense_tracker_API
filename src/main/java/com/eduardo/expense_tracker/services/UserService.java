package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.User;
import com.eduardo.expense_tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    @Autowired
    UserRepository repository;

    public User insertUser(User user){
        return repository.save(user);
    }
    public User userFindById(Long id){
        return repository.findById(id).orElse(null);
    }
    public List<User> userFindAll(){
        return repository.findAll();
    }
    public void deleteUser(Long id){
        repository.deleteById(id);
    }
     public User updateUser(Long id, User obj){
         User userFind = repository.findById(id).orElse(null);
         if (userFind != null) {
             updateData(userFind, obj);
             return repository.save(userFind);
         }
         return null;
     }
     private void updateData(User userFind, User obj) {
         userFind.setName(obj.getName());
         userFind.setEmail(obj.getEmail());
         userFind.setPhone(obj.getPhone());
     }
}
