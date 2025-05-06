package com.cloudSerenityHotel.attraction_facility.facility.service;

import java.util.List;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

public interface FacilityService {
    // 根據設施名稱查詢設施
    Facility findFacilityByName(String name);
    
    // 查詢所有設施
    List<Facility> findAllFacilities();
    
    // 刪除設施
    boolean deleteFacility(int id);
    
    // 新增設施
    boolean addFacility(Facility facility);
    
    // 更新設施
    boolean updateFacility(Facility facility);
}
