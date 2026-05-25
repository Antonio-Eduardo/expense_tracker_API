package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

     public Location insertLocation(Location location){
        return repository.save(location);
     }
    public Location findLocationById(Long id){
         return repository.findById(id).orElse(null);
    }
     public List<Location> findAllLocations(){
         return repository.findAll();
     }
     public void deleteLocation(Long id) {
         repository.deleteById(id);
     }
     public void updateData(Location locationFind, Location obj){
        locationFind.setCity(obj.getCity());
        locationFind.setState(obj.getState());
        locationFind.setAddress1(obj.getAddress1());
        locationFind.setAddress2(obj.getAddress2());
        locationFind.setZipCode(obj.getZipCode());
     }
     public Location locationUpdate(Long id, Location obj) {
         Location locationFind = repository.findById(id).orElse(null);
         if (locationFind != null) {
             updateData(locationFind, obj);
              return repository.save(locationFind);
         }
         return null;
     }
}
