package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.UserDTO;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.entities.User;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;
    @Autowired
    LocationRepository locationRepository;

    public User insertUser(UserDTO user){
        User userDB = new User();
        Location location = locationRepository.findById(user.getLocationId()).orElseThrow(() -> new ResourceNotFind("Location not found with id: " + user.getLocationId()));
        userDB.setName(user.getName());
        userDB.setEmail(user.getEmail());
        userDB.setPhone(user.getPhone());
        userDB.setPassword(user.getPassword());
        userDB.setLocation(location);
        userDB.setCpf(user.getCpf());
        userDB.setBirthDate(user.getBirthDate());
        return repository.save(userDB);
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
