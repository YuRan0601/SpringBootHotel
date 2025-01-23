//package com.cloudSerenityHotel.attraction_facility.attraction.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/CloudSerenityHotel/api/images")
//public class ImageUploadController {
//
//    private static final String UPLOAD_DIR = "/SpringBootHotel/src/main/webapp/static/attraction/uploads/";
//    
//    @GetMapping("/test")
//    public ResponseEntity<String> testApi() {
//        return ResponseEntity.ok("API is working correctly!");
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("Please select a file to upload");
//        }
//
//        try {
//            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String randomFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
//
//            File uploadDir = new File(UPLOAD_DIR);
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();
//            }
//
//            String filePath = Paths.get(UPLOAD_DIR, randomFileName).toString();
//            file.transferTo(new File(filePath));
//
//            String fileUrl = "/CloudSerenityHotel/static/attraction/uploads/" + randomFileName;
//            return ResponseEntity.ok("http://localhost:8080/CloudSerenityHotel/static/attraction/uploadSuccess.html?url=" + fileUrl);
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading file");
//        }
//    }
//}
