package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetAllAttractions")
public class GetAllAttractions extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Attraction> attractionList = null;
        Session session = null;

        try {
            // 開啟 Hibernate Session
            session = HibernateUtil.getSessionFactory().openSession();

            // 使用 HQL 查詢所有 Attraction 資料
            String hql = "FROM Attraction";  // 這裡使用 HQL (Hibernate Query Language)
            Query<Attraction> query = session.createQuery(hql, Attraction.class);
            attractionList = query.getResultList();  // 執行查詢並返回結果列表

            // 將查詢結果傳遞到 JSP 頁面
            request.setAttribute("attractionList", attractionList);
            request.getRequestDispatcher("/static/attraction/GetAllAttractions.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // 如果出現錯誤，可以處理錯誤並顯示給用戶
        } finally {
            if (session != null) {
                session.close();  // 關閉 Hibernate Session
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
