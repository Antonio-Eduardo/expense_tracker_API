package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.LocationDTOrequest;
import com.eduardo.expense_tracker.dtos.response.LocationDTOresponse;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@Tag(name = "Locations", description = "Operações relacionadas às Localizações")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    @Operation(summary = "Lista todas as localizações")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<LocationDTOresponse>> findAll() {
        return ResponseEntity.ok().body(locationService.findAllLocations());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar localização por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Localização não encontrada")
    public ResponseEntity<LocationDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(locationService.findLocationById(id));
    }
    @PostMapping(value = "/insert")
    @Operation(summary = "Insere uma nova localização")
    @ApiResponse(responseCode = "201", description = "Localização criada com sucesso")
    public ResponseEntity<LocationDTOresponse> insertLocation(@RequestBody LocationDTOrequest data) {
        LocationDTOresponse locationDTO = locationService.insertLocation(data);
        return ResponseEntity.ok().body(locationDTO);
    }
    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Atualiza uma localização")
    @ApiResponse(responseCode = "200", description = "Localização atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Localização não encontrada")
    public ResponseEntity<LocationDTOresponse> updateLocation(@PathVariable Long id, @RequestBody LocationDTOrequest locationDTO) {
      return ResponseEntity.ok().body(locationService.locationUpdate(id, locationDTO));
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Exclui uma localização")
    @ApiResponse(responseCode = "204", description = "Localização excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Localização não encontrada")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

}
