package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetAttractionJNDI")
public class GetAttractionJNDI extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name"); // 從用戶輸入獲取景點名稱
        Connection conn = null;

        try {
            // 獲取 JNDI 資料源
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/CloudSerenityHotel");
            conn = ds.getConnection();

            // 查詢景點資料
            String sql = "SELECT * FROM Attraction WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            Attraction attraction = null;
            if (rs.next()) {
                attraction = new Attraction(
//                        rs.getInt("attraction_id"),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getString("location"),
//                        rs.getString("opening_hours"),
//                        rs.getString("contact_info"),
//                        rs.getInt("type_id"),
//                        rs.getString("image_url"),
//                        rs.getTimestamp("create_at"),
//                        rs.getTimestamp("update_at")
                );
            }

            // 將結果存入 request 並轉發到 JSP
            if (attraction != null) {
                request.setAttribute("attraction", attraction);
                request.getRequestDispatcher("/GetAttraction.jsp").forward(request, response);
            } else {
                response.getWriter().write("找不到該景點資料");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("資料查詢失敗");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
