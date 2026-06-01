package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.RegisterDTOrequest;
import com.eduardo.expense_tracker.dtos.request.UserDTOrequest;
import com.eduardo.expense_tracker.dtos.response.UserDTOresponse;
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

     public UserDTOresponse updateUser(Long id, UserDTOrequest obj){
         User userFind = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found" + id));
         updateData(userFind,obj);
         userFind = repository.save(userFind);
         return convertToResponseDTO(userFind);
     }
     private void updateData(User userFind, UserDTOrequest obj) {
         Location location = locationRepository.findById(obj.getLocationId())
                 .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + obj.getLocationId()));

            userFind.setName(obj.getName());
            userFind.setPhone(obj.getPhone());
            userFind.setLocation(location);
            userFind.setCpf(obj.getCpf());
            userFind.setBirthDate(obj.getBirthDate());
     }
    public UserDTOresponse createUser(RegisterDTOrequest data) {
        User user = new User();
        user.setEmail(data.email());
        user.setPassword(data.password());
        user.setRole(data.role());
        user = repository.save(user);
        return convertToResponseDTO(user);
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    public UserDTOresponse convertToResponseDTO(User user){
        UserDTOresponse response = new UserDTOresponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setCpf(user.getCpf());
        response.setPhone(user.getEmail());
        response.setBirthDate(user.getBirthDate());
        if (user.getLocation() != null){
            response.setLocationId(user.getLocation().getId());
        }
        return response;
    }
}
