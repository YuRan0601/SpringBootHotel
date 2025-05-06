package com.cloudSerenityHotel.attraction_facility.attraction.service;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

import java.util.List;
import java.util.Optional;

public interface AttractionService {
    List<Attraction> getAllAttractions();
    Optional<Attraction> getAttractionById(Integer id);
    Attraction createAttraction(Attraction attraction);
    void deleteAttraction(Integer id);
    Attraction updateAttraction(Integer id, Attraction updatedAttraction);
}
