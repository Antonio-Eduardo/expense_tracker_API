package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.LocationDTO;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    @Transactional
     public LocationDTO insertLocation(LocationDTO locationDTO){
        Location location = new Location();
        location.setCity(locationDTO.getCity());
        location.setState(locationDTO.getState());
        location.setAddress1(locationDTO.getAddress1());
        location.setAddress2(locationDTO.getAddress2());
        location.setZipCode(locationDTO.getZipCode());

        Location savedLocation = repository.save(location);

        LocationDTO response = new LocationDTO();
        response.setCity(savedLocation.getCity());
        response.setZipCode(savedLocation.getZipCode());
        response.setState(savedLocation.getState());
        response.setAddress1(savedLocation.getAddress1());
        response.setAddress2(savedLocation.getAddress2());

         return response;
     }
    public Location findLocationById(Long id){
         return repository.findById(id).orElse(null);
    }
     public List<Location> findAllLocations(){
         return repository.findAll();
     }
     @Transactional
     public void deleteLocation(Long id) {
         repository.deleteById(id);
     }
     public void updateData(Location locationFind, LocationDTO obj){
        locationFind.setCity(obj.getCity());
        locationFind.setState(obj.getState());
        locationFind.setAddress1(obj.getAddress1());
        locationFind.setAddress2(obj.getAddress2());
        locationFind.setZipCode(obj.getZipCode());
     }
     @Transactional
     public Location locationUpdate(Long id, LocationDTO obj) {
         Location locationFind = repository.findById(id).orElse(null);
         if (locationFind != null) {
             updateData(locationFind, obj);
              return repository.save(locationFind);
         }
         return null;
     }
}
