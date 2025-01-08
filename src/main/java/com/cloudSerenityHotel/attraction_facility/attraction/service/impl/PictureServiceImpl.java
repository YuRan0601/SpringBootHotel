package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.attraction_facility.attraction.repository.PictureRepository;
import com.cloudSerenityHotel.attraction_facility.attraction.service.PictureService;

import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {

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
        if (pictureRepository.existsById(id)) {
            updatedPicture.setImageId(id);
            return pictureRepository.save(updatedPicture);
        }
        return null;
    }
}
