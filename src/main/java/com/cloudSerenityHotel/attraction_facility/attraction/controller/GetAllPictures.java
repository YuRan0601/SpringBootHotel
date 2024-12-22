package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/GetAllPictures")
public class GetAllPictures extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PictureDAO pictureDAO = new PictureDAO(); // PictureDAO 實例

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Picture> pictureList = pictureDAO.getAll(); // 使用 Hibernate 查詢所有圖片資料

        // 設定 request 屬性以供 JSP 使用
        request.setAttribute("pictureList", pictureList);

        // 導向到顯示所有圖片資料的 JSP 頁面
        request.getRequestDispatcher("/static/picture/GetAllPictures.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
