package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.RegisterDTOrequest;
import com.eduardo.expense_tracker.dtos.request.UserDTOrequest;
import com.eduardo.expense_tracker.dtos.response.RegisterDTOresponse;
import com.eduardo.expense_tracker.dtos.response.UserDTOresponse;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.exceptions.DuplicateResourceException;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private LocationRepository locationRepository;

    public UserDTOresponse userFindById(Long id){
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "User not Found" + id));

        return convertToUserResponseDTO(user);
    }

    public List<UserDTOresponse> userFindAll(){
        return repository.findAll().stream()
                .map(this::convertToUserResponseDTO).toList();
    }

    @Transactional
    public void deleteUser(Long id){
        repository.deleteById(id);
    }

    @Transactional
     public UserDTOresponse updateUser(Long id, UserDTOrequest obj){
         User userFind = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found" + id));
         updateData(userFind,obj);
         userFind = repository.save(userFind);
         return convertToUserResponseDTO(userFind);
     }
     private void updateData(User userFind, UserDTOrequest obj) {
        if (obj.getName() != null) {
            userFind.setName(obj.getName());
        }
        if (obj.getPhone() !=  null) {
            userFind.setPhone(obj.getPhone());
        }
        if (obj.getLocationId() != null){
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
     @Transactional
    public RegisterDTOresponse createUser(RegisterDTOrequest data) {
        if (repository.findByEmail(data.email()).isPresent()) {
            throw new DuplicateResourceException("Email already in use: " + data.email());
        }
        User user = new User();
        user.setEmail(data.email());
        user.setPassword(data.password());
        user.setRole(data.role());
        user = repository.save(user);
        return convertToRegisterDTO(user);
    }
    public UserDTOresponse findByEmail(String email) {
       User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToUserResponseDTO(user);
    }
    public UserDTOresponse convertToUserResponseDTO(User user){
        UserDTOresponse response = new UserDTOresponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCpf(user.getCpf());
        response.setPhone(user.getPhone());
        response.setBirthDate(user.getBirthDate());
        if (user.getLocation() != null){
            response.setLocationId(user.getLocation().getId());
        }
        return response;
    }
    public RegisterDTOresponse convertToRegisterDTO(User user){
        RegisterDTOresponse response = new RegisterDTOresponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
