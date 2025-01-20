package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.attraction_facility.attraction.service.PictureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SpringBootApplication
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @GetMapping("/")
    public List<Picture> getAllPictures() {
        return pictureService.getAllPictures();
    }

    @GetMapping("/{id}")
    public Optional<Picture> getPictureById(@PathVariable Integer id) {
        return pictureService.getPictureById(id);
    }

    @PostMapping("/")
    public Picture createPicture(@RequestBody Picture picture) {
        return pictureService.createPicture(picture);
    }

    @PutMapping("/{id}")
    public Picture updatePicture(@PathVariable Integer id, @RequestBody Picture updatedPicture) {
        return pictureService.updatePicture(id, updatedPicture);
    }

    @DeleteMapping("/{id}")
    public void deletePicture(@PathVariable Integer id) {
        pictureService.deletePicture(id);
    }
}