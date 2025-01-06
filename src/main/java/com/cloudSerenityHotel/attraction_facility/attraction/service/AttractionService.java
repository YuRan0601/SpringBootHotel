package com.cloudSerenityHotel.attraction_facility.attraction.service;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

import java.util.List;
import java.util.Optional;

public interface AttractionService {  // 確保這是interface
	

	
    List<Attraction> getAllAttractions();

    Optional<Attraction> getAttractionById(int id);

    Attraction addAttraction(Attraction attraction);

    Attraction updateAttraction(int id, Attraction attraction);

    void deleteAttraction(int id);
}

