package com.cloudSerenityHotel.attraction_facility.facility.controller;

import java.io.IOException;
import java.sql.Timestamp;

import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdateFacility")
public class UpdateFacility extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 安全地解析請求參數
        Integer facilityId = parseInteger(request.getParameter("facilityId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String availabilityHours = request.getParameter("availabilityhours");  // 更改為 availability_hours

        // 取得當前時間
        Timestamp updateAt = new Timestamp(System.currentTimeMillis());

     // 創建 Facility 物件
        Facility facility = new Facility();
        facility.setFacilityId(facilityId);
        facility.setName(name);
        facility.setDescription(description);
        facility.setAvailabilityHours(availabilityHours);
        facility.setLocation(location);
        facility.setUpdateAt(updateAt);


        // 檢查 facilityId 是否有效
        if (facilityId == null) {
            // 處理錯誤情況，例如跳轉到錯誤頁
            response.sendRedirect("error.jsp?message=Facility ID is required");
            return;
        }

        // 呼叫 DAO 來更新資料
        FacilityDAO facilityDAO = new FacilityDAO();
        boolean success = facilityDAO.update(facility);

        // 重定向至對應的頁面
        if (success) {
            response.sendRedirect("/CloudSerenityHotel/Facility/GetAllFacilities?facilityId=" + facilityId);
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
