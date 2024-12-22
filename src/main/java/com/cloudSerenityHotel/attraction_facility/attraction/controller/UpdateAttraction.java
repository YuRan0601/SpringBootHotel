package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;
import java.sql.Timestamp;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.AttractionDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdateAttraction")
public class UpdateAttraction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 安全地解析請求參數
        Integer attractionId = parseInteger(request.getParameter("attractionId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String openingHours = request.getParameter("openingHours");
        String contactInfo = request.getParameter("contactInfo");
        String imageUrl = request.getParameter("imageUrl");
        
        // 取得當前時間
        Timestamp updateAt = new Timestamp(System.currentTimeMillis());
        
        // 安全解析 type_id，允許為空
        Integer typeId = parseInteger(request.getParameter("type_id"));

        // 創建 Attraction 物件
        Attraction attraction = new Attraction();
        attraction.setAttractionId(attractionId);
        attraction.setName(name);
        attraction.setDescription(description);
        attraction.setLocation(location);
        attraction.setOpeningHours(openingHours);
        attraction.setContactInfo(contactInfo);
        attraction.setTypeId(typeId);
        attraction.setImageUrl(imageUrl);
        attraction.setUpdateAt(updateAt);  // 設置更新時間

        // 呼叫 DAO 來更新資料
        AttractionDAO attractionDAO = new AttractionDAO();
        boolean success = attractionDAO.updateAttraction(attraction);  // 使用 Hibernate 進行更新

        // 重定向至對應的頁面
        if (success) {
            response.sendRedirect("GetAllAttractions?attractionId=" + attractionId);
        } else {
            response.sendRedirect("/static/picture/error.jsp");
        }
    }

    /**
     * 安全解析整數方法，允許 null 或無效值
     * 
     * @param value 要解析的字串
     * @return 解析後的整數或 null
     */
    private Integer parseInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty()) ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null; // 無效值時返回 null
        }
    }
}