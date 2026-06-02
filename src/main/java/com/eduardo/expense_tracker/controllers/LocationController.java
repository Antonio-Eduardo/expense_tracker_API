package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.LocationDTOrequest;
import com.eduardo.expense_tracker.dtos.response.LocationDTOresponse;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDTOresponse>> findAll() {
        return ResponseEntity.ok().body(locationService.findAllLocations());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LocationDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(locationService.findLocationById(id));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<LocationDTOresponse> insertLocation(@RequestBody LocationDTOrequest data) {
        LocationDTOresponse locationDTO = locationService.insertLocation(data);
        return ResponseEntity.ok().body(locationDTO);
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<LocationDTOresponse> updateLocation(@PathVariable Long id, @RequestBody LocationDTOrequest locationDTO) {
      return ResponseEntity.ok().body(locationService.locationUpdate(id, locationDTO));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

}
