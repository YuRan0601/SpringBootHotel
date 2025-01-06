package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/")
    public List<Attraction> getAllAttractions() {
        return attractionService.getAllAttractions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attraction> getAttractionById(@PathVariable int id) {
        Optional<Attraction> attraction = attractionService.getAttractionById(id);
        return attraction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public Attraction createAttraction(@RequestBody Attraction attraction) {
        return attractionService.addAttraction(attraction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attraction> updateAttraction(@PathVariable int id, @RequestBody Attraction attraction) {
        Attraction updatedAttraction = attractionService.updateAttraction(id, attraction);
        return updatedAttraction != null ? ResponseEntity.ok(updatedAttraction) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable int id) {
        attractionService.deleteAttraction(id);
        return ResponseEntity.noContent().build();
        
        
    }
}
