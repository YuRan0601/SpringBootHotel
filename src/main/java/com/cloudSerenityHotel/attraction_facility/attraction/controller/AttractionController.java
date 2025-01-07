package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SpringBootApplication
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/")
    public List<Attraction> getAllAttractions() {
        return attractionService.getAllAttractions();
    }

    @GetMapping("/{id}")
    public Optional<Attraction> getAttractionById(@PathVariable Integer id) {
        return attractionService.getAttractionById(id);
    }

    @PostMapping("/")
    public Attraction createAttraction(@RequestBody Attraction attraction) {
        return attractionService.createAttraction(attraction);
    }

    @PutMapping("/{id}")
    public Attraction updateAttraction(@PathVariable Integer id, @RequestBody Attraction updatedAttraction) {
        return attractionService.updateAttraction(id, updatedAttraction);
    }

    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable Integer id) {
        attractionService.deleteAttraction(id);
    }
}
