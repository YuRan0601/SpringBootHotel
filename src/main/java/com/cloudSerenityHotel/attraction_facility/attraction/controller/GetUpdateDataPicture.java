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

@WebServlet("/GetUpdateDataPicture")
public class GetUpdateDataPicture extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 建立 DAO 實例
    private final PictureDAO pictureDAO = new PictureDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 獲取前端傳來的參照ID和參照類型
        String referenceIdStr = request.getParameter("reference_id");
        String referenceType = request.getParameter("reference_type");

        // 初始化圖片資料列表
        List<Picture> pictures = null;

        // 根據 referenceId 和 referenceType 查詢圖片資料
        if (referenceIdStr != null && !referenceIdStr.isEmpty() && referenceType != null && !referenceType.isEmpty()) {
            int referenceId = Integer.parseInt(referenceIdStr);
            pictures = pictureDAO.getPicturesByReference(referenceId, referenceType);
        }

        // 將查詢到的圖片資料存入 request
        if (pictures != null && !pictures.isEmpty()) {
            request.setAttribute("pictures", pictures);
        } else {
            request.setAttribute("message", "找不到對應的圖片資料");
        }

        // 轉發到 JSP，顯示圖片資料
        request.getRequestDispatcher("/static/picture/UpdateDataPicture.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST 請求同樣調用 doGet 方法
        doGet(request, response);
    }
}
