package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.LocationDTOrequest;
import com.eduardo.expense_tracker.dtos.response.LocationDTOresponse;
import com.eduardo.expense_tracker.dtos.response.MonthlyExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.LocationRepository;
import com.eduardo.expense_tracker.services.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void deveriaCriarUmaLocalizacao(){
        Location location = new Location();
        location.setCity("Fortaleza");

        LocationDTOrequest locationDTOrequest = new LocationDTOrequest();
        locationDTOrequest.setCity(location.getCity());
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTOresponse result = locationService.insertLocation(locationDTOrequest);

        assertNotNull(result);
        assertEquals("Fortaleza", result.getCity());
        verify(locationRepository).save(any(Location.class));
    }
    @Test
    public void deveriaAcharUmaLocalizacaoPorId(){
        Location location = new Location();
        location.setId(1L);

        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(location));

        LocationDTOresponse result = locationService.findLocationById(location.getId());
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(locationRepository).findById(any(Long.class));
    }
    @Test
    public void deveriaDeletarUmaLocalizacaoPorId(){
        locationService.deleteLocation(1L);
        verify(locationRepository).deleteById(any(Long.class));
    }
    @Test
    public void deveriaRetornarTodasAsLocalizacoes(){
        when(locationRepository.findAll()).thenReturn(java.util.List.of(new Location(), new Location()));

        List<LocationDTOresponse> result = locationService.findAllLocations();

        assertEquals(2, result.size());
        assertNotNull(result);
        verify(locationRepository).findAll();
    }
    @Test
    public void deveriaAtualizarUmaLocalizacaoPeloId(){
        Location location = new Location();
        location.setId(1L);
        location.setCity("Fortaleza");

        LocationDTOrequest locationDTOrequest = new LocationDTOrequest();
        locationDTOrequest.setCity("São Paulo");

        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTOresponse result = locationService.locationUpdate(1L, locationDTOrequest);

        assertNotNull(result);
        assertEquals("São Paulo", result.getCity());
        verify(locationRepository).findById(any(Long.class));
        verify(locationRepository).save(any(Location.class));
    }
}
