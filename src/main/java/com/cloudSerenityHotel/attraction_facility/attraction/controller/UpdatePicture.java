package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/UpdatePicture")
public class UpdatePicture extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 安全地解析請求參數
        Integer imageId = parseInteger(request.getParameter("imageId"));
        Integer referenceId = parseInteger(request.getParameter("referenceId"));
        String referenceType = request.getParameter("referenceType");
        String imageUrl = request.getParameter("imageUrl");

        // 取得當前時間
        Timestamp updateAt = new Timestamp(System.currentTimeMillis());

        // 創建 Picture 物件
        Picture picture = new Picture();
        picture.setImageId(imageId);
        picture.setReferenceId(referenceId);
        picture.setReferenceType(referenceType);
        picture.setImageUrl(imageUrl);
        picture.setUpdateAt(updateAt);

        // 檢查 imageId 是否有效
        if (imageId == null) {
            // 處理錯誤情況
            response.sendRedirect("error.jsp?message=Image ID is required");
            return;
        }

        // 呼叫 DAO 來更新資料
        PictureDAO pictureDAO = new PictureDAO();
        boolean success = pictureDAO.update(picture);

        // 重定向至對應的頁面
        if (success) {
            response.sendRedirect("GetPicture?referenceId=" + referenceId + "&referenceType=" + referenceType);
        } else {
            response.sendRedirect("error.jsp?message=Failed to update the picture");
        }
    }

    /**
     * 安全解析整數方法，允許 null 或無效值
     * 
     * @param value 要解析的字串
     * @return 解析後的整數或 null
     */
    private Integer parseInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty()) ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null; // 無效值時返回 null
        }
    }
}
