package com.popsa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;

class PhotoServiceTest {

    ReverseGeocodeService reverseGeocodeService;

    PhotoService photoService;

    @BeforeEach
    void setUp() {
        reverseGeocodeService = mock(ReverseGeocodeService.class);
        photoService = new PhotoService(reverseGeocodeService);
    }

    @Test
    void getTitle() {
        Mockito.when(reverseGeocodeService.getCity(anyDouble(), anyDouble())).thenReturn(new String[]{"Istanbul", "Turkey"});
        String title = photoService.getTitle("1.csv");
        String result = title.substring(title.length() - 18);
        assertEquals("Istanbul on Monday", result);
    }

}