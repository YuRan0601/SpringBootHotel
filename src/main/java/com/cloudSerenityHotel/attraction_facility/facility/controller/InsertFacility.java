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

@WebServlet("/InsertFacility")
public class InsertFacility extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 建立 DAO 實例
    private final FacilityDAO facilityDAO = new FacilityDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 獲取前端傳來的設施資料
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String availabilityHours = request.getParameter("availabilityhours");
        Timestamp createAt = new Timestamp(System.currentTimeMillis());  // 設定當前時間為創建時間
        Timestamp updateAt = new Timestamp(System.currentTimeMillis());  // 設定當前時間為更新時間

        // 初始化 Facility 並設置資料
        Facility facility = new Facility();
        facility.setName(name);
        facility.setDescription(description);
        facility.setLocation(location);
        facility.setAvailabilityHours(availabilityHours);
        facility.setCreateAt(createAt);
        facility.setUpdateAt(updateAt);

        try {
            // 使用 DAO 插入設施資料
            facilityDAO.insertFacility(facility);

            // 重定向到顯示所有設施資料的頁面
            response.sendRedirect(request.getContextPath() + "/Facility/GetAllFacilities");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}