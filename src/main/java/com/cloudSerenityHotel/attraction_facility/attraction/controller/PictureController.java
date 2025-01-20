package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.attraction_facility.attraction.service.PictureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@SpringBootApplication
@RequestMapping("/api/pictures")
public class PictureController {
	
	@PostMapping("/upload")
    public ResponseEntity<List<Picture>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<Picture> savedPictures = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                Picture picture = pictureService.saveFile(file);
                savedPictures.add(picture);
            }
            return ResponseEntity.ok(savedPictures);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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