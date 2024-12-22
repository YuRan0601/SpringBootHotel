package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Picture/DeletePicture")
public class DeletePicture extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PictureDAO pictureDAO = new PictureDAO(); // 建立 PictureDAO 實例

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imageIdStr = request.getParameter("image_id"); // 接收表單中的 image_id

        if (imageIdStr != null) {
            try {
                int imageId = Integer.parseInt(imageIdStr); // 解析為圖片 ID
                Picture picture = pictureDAO.getOne(imageId); // 根據圖片 ID 獲取圖片

                if (picture != null) {
                    pictureDAO.delete(imageId); // 使用 Hibernate 進行刪除操作
                    request.setAttribute("message", "圖片資料刪除成功!");
                } else {
                    request.setAttribute("message", "找不到指定的圖片資料，刪除失敗!");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("message", "圖片 ID 格式錯誤!");
            } catch (Exception e) {
                request.setAttribute("message", "刪除過程中發生錯誤: " + e.getMessage());
            }
        } else {
            request.setAttribute("message", "未提供圖片 ID!");
        }

        request.getRequestDispatcher("/static/picture/DeletePicture.jsp").forward(request, response); // 導向結果頁面
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
