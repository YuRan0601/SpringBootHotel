package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cloudSerenityHotel.attraction_facility.attraction.dao.PictureDAO;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetPictureJNDI")
public class GetPictureJNDI extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PictureDAO pictureDAO;

    public GetPictureJNDI() {
        this.pictureDAO = new PictureDAO(); // 初始化 PictureDAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int referenceId = Integer.parseInt(request.getParameter("referenceId")); // 獲取 referenceId
        String referenceType = request.getParameter("referenceType"); // 獲取 referenceType

        List<Picture> pictureList = null;
        
        try {
            // 查詢圖片資料
            pictureList = pictureDAO.getPicturesByReference(referenceId, referenceType);

            // 將結果存入 request 並轉發到 JSP
            if (pictureList != null && !pictureList.isEmpty()) {
                request.setAttribute("pictureList", pictureList);
                request.getRequestDispatcher("/GetPicture.jsp").forward(request, response);
            } else {
                response.getWriter().write("找不到該圖片資料");
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
