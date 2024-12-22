package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;



@WebServlet("/GetAllPicturesJNDI")
public class GetAllPicturesJNDI extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PictureDAO pictureDAO;

    public GetAllPicturesJNDI() {
        this.pictureDAO = new PictureDAO(); // 初始化 PictureDAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Picture> pictureList = null;

        try {
            // 查詢所有圖片資料
            pictureList = pictureDAO.getAll();

            // 將結果存入 request 並轉發到 JSP
            if (pictureList != null && !pictureList.isEmpty()) {
                request.setAttribute("pictureList", pictureList);
                request.getRequestDispatcher("/GetAllPictures.jsp").forward(request, response);
            } else {
                response.getWriter().write("找不到任何圖片資料");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("資料查詢失敗");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
