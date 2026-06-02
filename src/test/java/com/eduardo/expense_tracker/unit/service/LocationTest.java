package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.repositories.LocationRepository;
import com.eduardo.expense_tracker.services.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocationTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void deveriaCriarUmaLocalizacao(){

    }
}
