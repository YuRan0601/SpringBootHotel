package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;
import com.cloudSerenityHotel.attraction_facility.attraction.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    @Override
    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    @Override
    public Optional<Attraction> getAttractionById(int id) {
        return attractionRepository.findById(id);
    }

    @Override
    public Attraction addAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    @Override
    public Attraction updateAttraction(int id, Attraction attraction) {
        if (attractionRepository.existsById(id)) {
            attraction.setAttractionId(id);
            return attractionRepository.save(attraction);
        } else {
            return null;  // 或拋出異常
        }
    }

    @Override
    public void deleteAttraction(int id) {
        attractionRepository.deleteById(id);
    }
}
