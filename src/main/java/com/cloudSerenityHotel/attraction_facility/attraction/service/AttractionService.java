package com.cloudSerenityHotel.attraction_facility.attraction.service;

import java.util.List;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

public interface AttractionService {
    // 根據景點名稱查詢景點
    Attraction findAttractionByName(String name);
    
    // 查詢所有景點
    List<Attraction> findAllAttractions();
    
    // 刪除景點
    boolean deleteAttraction(int id);
    
    // 新增景點
    boolean addAttraction(Attraction attraction);
}
