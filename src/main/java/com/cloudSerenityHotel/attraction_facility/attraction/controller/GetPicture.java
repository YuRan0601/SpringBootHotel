package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/GetPicture")
public class GetPicture extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PictureDAO pictureDAO = new PictureDAO(); // 使用 PictureDAO 來操作資料庫

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referenceId = request.getParameter("referenceId"); // 從請求中取得圖片參考ID

        // 使用 Hibernate 查詢圖片資料
        Picture picture = pictureDAO.getByReferenceId(referenceId);

        if (picture != null) {
            // 將查詢結果存入 request 並轉發到 JSP
            request.setAttribute("picture", picture);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/static/picture/GetPicture.jsp");
            dispatcher.forward(request, response);
        } else {
            // 當找不到資料時
            response.getWriter().write("No data found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
