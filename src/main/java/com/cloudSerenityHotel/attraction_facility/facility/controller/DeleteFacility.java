package com.cloudSerenityHotel.attraction_facility.facility.controller;

import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Facility/DeleteFacility")
public class DeleteFacility extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 使用 DAO 操作資料庫
    private FacilityDAO facilityDAO;

    public DeleteFacility() {
        super();
        // 初始化 FacilityDAO
        facilityDAO = new FacilityDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String facilityId = request.getParameter("facility_id"); // 接收表單中的 facility_id
        try {
            // 使用 DAO 刪除設施資料
            Facility facility = facilityDAO.getOne(Integer.parseInt(facilityId));
            if (facility != null) {
                facilityDAO.delete(Integer.parseInt(facilityId));  // 執行刪除操作
                request.setAttribute("message", "設施資料刪除成功!");
            } else {
                request.setAttribute("message", "找不到指定的設施資料，刪除失敗!");
            }
            request.getRequestDispatcher("/static/facility/DeleteFacility.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "刪除過程中發生錯誤: " + e.getMessage());
            request.getRequestDispatcher("/static/facility/DeleteFacility.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
