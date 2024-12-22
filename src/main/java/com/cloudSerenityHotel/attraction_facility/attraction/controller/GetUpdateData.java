package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.AttractionDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetUpdateData")
public class GetUpdateData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 建立 DAO 實例
    private final AttractionDAO attractionDAO = new AttractionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 獲取前端傳來的景點名稱
        String name = request.getParameter("name");

        // 根據名稱查詢景點資料
        Attraction attraction = attractionDAO.getOneByName(name);

        if (attraction != null) {
            // 將查詢到的景點資料存入 request
            request.setAttribute("attraction", attraction);
            // 轉發到 JSP，顯示更新表單
            request.getRequestDispatcher("/static/attraction/UpdateData.jsp")
                   .forward(request, response);
        } else {
            // 如果找不到景點資料，顯示錯誤訊息或跳轉至錯誤頁面
            request.setAttribute("errorMessage", "Attraction not found");
            request.getRequestDispatcher("/static/attraction/error.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST 請求同樣調用 doGet 方法
        doGet(request, response);
    }
}
