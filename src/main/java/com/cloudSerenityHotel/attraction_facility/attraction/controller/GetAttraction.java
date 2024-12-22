package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Attraction/GetAttraction")
public class GetAttraction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        Attraction attraction = null;
        Session session = null;

        try {
            // 開啟 Hibernate Session
            session = HibernateUtil.getSessionFactory().openSession();

            // 使用 HQL 查詢指定景點資料
            String hql = "FROM Attraction WHERE name = :name";  // 使用命名參數 :name
            Query<Attraction> query = session.createQuery(hql, Attraction.class);
            query.setParameter("name", name);  // 設定查詢參數
            attraction = query.uniqueResult();  // 獲取唯一結果

            // 如果找到了景點資料，設定為 request 屬性
            if (attraction != null) {
                request.setAttribute("attraction", attraction);
            } else {
                request.setAttribute("message", "找不到該景點資料!");
            }

            // 導向至 JSP 頁面
            request.getRequestDispatcher("/static/attraction/GetAttraction.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "查詢過程中發生錯誤: " + e.getMessage());
            request.getRequestDispatcher("/static/attraction/GetAttraction.jsp").forward(request, response);
        } finally {
            // 確保 Session 關閉
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
