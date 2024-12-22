package com.cloudSerenityHotel.attraction_facility.facility.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetFacilityJNDI")
public class GetFacilityJNDI extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name"); // 從用戶輸入獲取設施名稱
        Connection conn = null;

        try {
            // 獲取 JNDI 資料源
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/cloud_serenity_hotel");
            conn = ds.getConnection();

            // 查詢設施資料
            String sql = "SELECT * FROM Facility WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            Facility facility = null;
            if (rs.next()) {
//                facility = new FacilityBean(
//                        rs.getInt("facility_id"),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getString("availability_hours"),
//                        rs.getString("location"),
//                        rs.getTimestamp("create_at"),
//                        rs.getTimestamp("update_at")
//                );
//            }

            // 將結果存入 request 並轉發到 JSP
            if (facility != null) {
                request.setAttribute("facility", facility);
                request.getRequestDispatcher("/GetFacility.jsp").forward(request, response);
            } else {
                response.getWriter().write("找不到該設施資料");
            }

            rs.close();
            stmt.close();
            }
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
