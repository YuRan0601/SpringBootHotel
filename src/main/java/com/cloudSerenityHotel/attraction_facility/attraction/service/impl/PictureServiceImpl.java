package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.attraction_facility.attraction.repository.PictureRepository;
import com.cloudSerenityHotel.attraction_facility.attraction.service.PictureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {

    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    @Override
    public Optional<Picture> getPictureById(Integer id) {
        return pictureRepository.findById(id);
    }

    @Override
    public Picture createPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public void deletePicture(Integer id) {
        pictureRepository.deleteById(id);
    }

    @Override
    public Picture updatePicture(Integer id, Picture updatedPicture) {
        Optional<Picture> existingPicture = pictureRepository.findById(id);
        if (existingPicture.isPresent()) {
            Picture picture = existingPicture.get();
            picture.setFileName(updatedPicture.getFileName());
            picture.setFilePath(updatedPicture.getFilePath());
            picture.setSize(updatedPicture.getSize());
            return pictureRepository.save(picture);
        }
        return null;
    }

    // 实现文件上传逻辑
    public Picture saveFile(MultipartFile file) throws IOException {
        // 确保上传目录存在
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // 创建 Picture 实体
        Picture picture = new Picture();
        picture.setFileName(fileName);
        picture.setFilePath(filePath.toString());
        picture.setSize(file.getSize());

        // 保存到数据库
        return pictureRepository.save(picture);
    }
}
