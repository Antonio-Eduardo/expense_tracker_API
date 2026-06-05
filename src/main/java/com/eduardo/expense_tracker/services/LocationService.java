package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.LocationDTOrequest;
import com.eduardo.expense_tracker.dtos.response.LocationDTOresponse;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    @Transactional
     public LocationDTOresponse insertLocation(LocationDTOrequest locationDTO){
        Location location = new Location();
        location.setCity(locationDTO.getCity());
        location.setState(locationDTO.getState());
        location.setAddress1(locationDTO.getAddress1());
        location.setAddress2(locationDTO.getAddress2());
        location.setZipCode(locationDTO.getZipCode());

        Location savedLocation = repository.save(location);

        LocationDTOresponse response = new LocationDTOresponse();
        response.setId(savedLocation.getId());
        response.setCity(savedLocation.getCity());
        response.setZipCode(savedLocation.getZipCode());
        response.setState(savedLocation.getState());
        response.setAddress1(savedLocation.getAddress1());
        response.setAddress2(savedLocation.getAddress2());

         return response;
     }
    public LocationDTOresponse findLocationById(Long id){
        Location location = repository.findById(id).orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        return convertToDTOresponse(location);
    }
     public List<LocationDTOresponse> findAllLocations(){
         return repository.findAll().stream().map(this::convertToDTOresponse).toList();
     }
     @Transactional
     public void deleteLocation(Long id) {
         repository.deleteById(id);
     }
     public void updateData(Location locationFind, LocationDTOrequest obj){
        if (obj.getCity() != null) {
            locationFind.setCity(obj.getCity());
        }
        if (obj.getState() != null) {
            locationFind.setState(obj.getState());
        }
        if (obj.getAddress1() != null) {
            locationFind.setAddress1(obj.getAddress1());
        }
        if (obj.getAddress2() != null) {
            locationFind.setAddress2(obj.getAddress2());
        }
        if (obj.getZipCode() != null) {
            locationFind.setZipCode(obj.getZipCode());
        }
     }
     @Transactional
     public LocationDTOresponse locationUpdate(Long id, LocationDTOrequest obj) {
         Location locationFind = repository.findById(id).orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
             updateData(locationFind, obj);
             locationFind = repository.save(locationFind);
              return convertToDTOresponse(locationFind);
     }

     public LocationDTOresponse convertToDTOresponse(Location location){
         LocationDTOresponse locationDTOresponse = new LocationDTOresponse();
         locationDTOresponse.setId(location.getId());
         locationDTOresponse.setCity(location.getCity());
         locationDTOresponse.setState(location.getState());
         locationDTOresponse.setAddress1(location.getAddress1());
         locationDTOresponse.setAddress2(location.getAddress2());
         locationDTOresponse.setZipCode(location.getZipCode());

         return locationDTOresponse;
     }
}
