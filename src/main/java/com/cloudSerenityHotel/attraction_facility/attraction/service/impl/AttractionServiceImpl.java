package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.repository.AttractionRepository;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;

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
    public Optional<Attraction> getAttractionById(Integer id) {
        return attractionRepository.findById(id);
    }

    @Override
    public Attraction createAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    @Override
    public void deleteAttraction(Integer id) {
        attractionRepository.deleteById(id);
    }

    @Override
    public Attraction updateAttraction(Integer id, Attraction updatedAttraction) {
        if (attractionRepository.existsById(id)) {
            updatedAttraction.setAttractionId(id);
            return attractionRepository.save(updatedAttraction);
        }
        return null;
    }
}
