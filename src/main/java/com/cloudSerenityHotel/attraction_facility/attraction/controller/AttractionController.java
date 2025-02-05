package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;

import jakarta.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@MultipartConfig
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;
    

    // 設定圖片上傳的目錄
    private static final String UPLOAD_DIR = "D:/hotel/workspace/SpringBootHotel/src/main/webapp/static/attraction/images/";


    @GetMapping("/")
    public List<Attraction> getAllAttractions() {
        return attractionService.getAllAttractions();
    }

    @GetMapping("/{id}")
    public Optional<Attraction> getAttractionById(@PathVariable Integer id) {
        return attractionService.getAttractionById(id);
    }
    
    @PostMapping("/upload")
    public Attraction uploadAttraction(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("openingHours") String openingHours,
            @RequestParam("contactInfo") String contactInfo,
            @RequestParam("typeId") Integer typeId,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            // 如果目錄不存在，則創建
            if (!Files.isDirectory(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 產生唯一檔案名稱，避免覆蓋
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, imageFile.getBytes());

            // 存入資料庫的圖片 URL
            String imageUrl = "http://localhost:8080/CloudSerenityHotel/static/attraction/images/" + fileName;

            // 建立 Attraction 物件
            Attraction attraction = new Attraction();
            attraction.setName(name);
            attraction.setDescription(description);
            attraction.setLocation(location);
            attraction.setOpeningHours(openingHours);
            attraction.setContactInfo(contactInfo);
            attraction.setTypeId(typeId);
            attraction.setImageUrl(imageUrl); // 存入 `image_url`

            // 儲存到資料庫
            return attractionService.createAttraction(attraction);

        } catch (Exception e) {
            throw new RuntimeException("圖片上傳失敗: " + e.getMessage());
        }
    }

//    @PostMapping("/")
//    public Attraction createAttraction(@RequestBody Attraction attraction) {
//        return attractionService.createAttraction(attraction);
//    }

    @PutMapping("/{id}")
    public Attraction updateAttraction(@PathVariable Integer id, @RequestBody Attraction updatedAttraction) {
        return attractionService.updateAttraction(id, updatedAttraction);
    }

    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable Integer id) {
        attractionService.deleteAttraction(id);
    }
}