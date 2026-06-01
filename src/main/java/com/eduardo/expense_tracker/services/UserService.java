package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.RegisterDTOrequest;
import com.eduardo.expense_tracker.dtos.request.UserDTOrequest;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private LocationRepository locationRepository;

    public User userFindById(Long id){
        return repository.findById(id).orElse(null);
    }
    public List<User> userFindAll(){
        return repository.findAll();
    }
    public void deleteUser(Long id){
        repository.deleteById(id);
    }
     public User updateUser(Long id, UserDTOrequest obj){

         User userFind = repository.findById(id).orElse(null);
         if (userFind != null) {
             updateData(userFind, obj);
             return repository.save(userFind);
         }
         return null;
     }
     private void updateData(User userFind, UserDTOrequest obj) {
        if (obj.getName() != null) {
            userFind.setName(obj.getName());
        }
        if (obj.getPhone() != null) {
            userFind.setPhone(obj.getPhone());
        }
        if (obj.getLocationId() != null) {
            Location location = locationRepository.findById(obj.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + obj.getLocationId()));
            userFind.setLocation(location);
        }
        if (obj.getCpf() != null) {
            userFind.setCpf(obj.getCpf());
        }
        if (obj.getBirthDate() != null) {
            userFind.setBirthDate(obj.getBirthDate());
        }
     }
    public User createUser(RegisterDTOrequest data) {
        User user = new User();
        user.setEmail(data.email());
        user.setPassword(data.password());
        user.setRole(data.role());
        return repository.save(user);
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
