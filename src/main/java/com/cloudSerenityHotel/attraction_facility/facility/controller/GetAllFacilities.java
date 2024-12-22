package com.cloudSerenityHotel.attraction_facility.facility.controller;

import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/Facility/GetAllFacilities")
public class GetAllFacilities extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 使用 DAO 層來處理資料查詢
    private FacilityDAO facilityDAO;

    public GetAllFacilities() {
        super();
        // 初始化 FacilityDAO
        facilityDAO = new FacilityDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 使用 DAO 來獲取所有設施資料
            List<Facility> facilityList = facilityDAO.getAll();

            // 設定 request 屬性，供 JSP 使用
            request.setAttribute("facilityList", facilityList);

            // 導向到顯示所有設施資料的 JSP 頁面
            request.getRequestDispatcher("/static/facility/GetAllFacilities.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "獲取設施資料時發生錯誤: " + e.getMessage());
            request.getRequestDispatcher("/static/facility/GetAllFacilities.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
