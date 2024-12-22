package com.cloudSerenityHotel.attraction_facility.facility.controller;

import java.io.IOException;

import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetUpdateDataFacility")
public class GetUpdateDataFacility extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 建立 DAO 實例
    private final FacilityDAO facilityDAO = new FacilityDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 獲取前端傳來的設施名稱
        String name = request.getParameter("name");

        // 初始化 Facility
        Facility facility = null;

        // 從 DAO 獲取設施資料
        facility = facilityDAO.getOneByName(name);

        // 將查詢到的設施資料存入 request
        if (facility != null) {
            request.setAttribute("facility", facility);
        } else {
            // 如果找不到設施資料，處理錯誤邏輯
            request.setAttribute("message", "找不到該設施資料");
        }

        // 轉發到 JSP，顯示更新表單
        request.getRequestDispatcher("/static/facility/UpdateDataFacility.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST 請求同樣調用 doGet 方法
        doGet(request, response);
    }
}
