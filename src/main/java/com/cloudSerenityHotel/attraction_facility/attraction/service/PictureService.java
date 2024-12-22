package com.cloudSerenityHotel.attraction_facility.attraction.service;

import java.util.List;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;

public interface PictureService {
    // 根據圖片ID查詢圖片
    Picture findPictureById(int id);
    
    // 查詢所有圖片
    List<Picture> findAllPictures();
    
    // 根據圖片ID刪除圖片
    boolean deletePicture(int id);
    
    // 新增圖片
    boolean addPicture(Picture picture);
    
    // 根據關聯ID和類型查詢圖片
    List<Picture> findPicturesByReference(int referenceId, String referenceType);
}
