package com.cloudSerenityHotel.attraction_facility.facility.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/GetAllFacilitiesJNDI")
public class GetAllFacilityJNDI extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Facility> facilityList = new ArrayList<>();
        
        try {
            // 取得JNDI資料源
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/cloud_serenity_hotel");
            try (Connection conn = dataSource.getConnection()) {
                String sql = "SELECT * FROM Facility";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                // 處理查詢結果並將其轉換為 FacilityBean 物件
//                while (rs.next()) {
//                    FacilityBean facility = new FacilityBean(
//                            rs.getInt("facility_id"),
//                            rs.getString("name"),
//                            rs.getString("description"),
//                            rs.getString("availabilityhours"),
//                            rs.getString("location"),
//                            rs.getTimestamp("create_at"),
//                            rs.getTimestamp("update_at")
//                    );
//                    facilityList.add(facility);
//                }

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("資料庫操作錯誤：" + e.getMessage());
        }

        // 將設施列表存入 request 並轉發到 JSP 頁面
        request.setAttribute("facilityList", facilityList);
        request.getRequestDispatcher("/GetAllFacilities.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
