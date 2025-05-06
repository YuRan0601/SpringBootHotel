package com.cloudSerenityHotel.attraction_facility.facility.controller;

import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Facility/GetFacility")
public class GetFacility extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 使用 FacilityDAO 來查詢設施
    private FacilityDAO facilityDAO;

    public GetFacility() {
        super();
        // 初始化 FacilityDAO
        facilityDAO = new FacilityDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name"); // 從請求中取得設施名稱

        try {
            // 使用 DAO 層查詢指定名稱的設施資料
            Facility facility = facilityDAO.getByName(name);

            // 判斷是否找到了設施資料，並將結果傳遞到 JSP 頁面
            if (facility != null) {
                request.setAttribute("facility", facility);
                request.getRequestDispatcher("/static/facility/GetFacility.jsp")
                        .forward(request, response);
            } else {
                response.getWriter().write("找不到該設施資料");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("資料查詢失敗");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
