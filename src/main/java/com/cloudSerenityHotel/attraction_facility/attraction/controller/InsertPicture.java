package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/InsertPicture")
public class InsertPicture extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 建立 DAO 實例
    private final PictureDAO pictureDAO = new PictureDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referenceIdStr = request.getParameter("reference_Id");  // 景點或設施ID
        String referenceType = request.getParameter("reference_Type");  // 關聯類型 (Attraction 或 Facility)
        String imageUrl = request.getParameter("image_Url");  // 圖片URL

        // 檢查參數有效性
        if (referenceIdStr != null && !referenceIdStr.isEmpty() && referenceType != null && !referenceType.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {
            int referenceId = Integer.parseInt(referenceIdStr);

            // 創建 Picture 物件
            Picture picture = new Picture();
            picture.setReferenceId(referenceId);
            picture.setReferenceType(referenceType);
            picture.setImageUrl(imageUrl);

            // 使用 DAO 插入資料
            pictureDAO.insertPicture(picture);

            // 重定向至顯示所有圖片頁面
            response.sendRedirect(request.getContextPath() + "/GetAllPictures");
        } else {
            // 如果參數無效，顯示錯誤消息
            request.setAttribute("message", "參數無效！");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
