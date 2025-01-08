package com.cloudSerenityHotel.attraction_facility.attraction.service;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;

import java.util.List;
import java.util.Optional;

public interface PictureService {
    List<Picture> getAllPictures();
    Optional<Picture> getPictureById(Integer id);
    Picture createPicture(Picture picture);
    void deletePicture(Integer id);
    Picture updatePicture(Integer id, Picture updatedPicture);
}
