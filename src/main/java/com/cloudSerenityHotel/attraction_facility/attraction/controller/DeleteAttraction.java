package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Attraction/DeleteAttraction")
public class DeleteAttraction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attractionId = request.getParameter("attraction_id"); // 接收表單中的 attraction_id
        Session session = null;
        Transaction transaction = null;

        try {
            // 初始化 Hibernate Session
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // 查找對應的 Attraction 物件
            int id = Integer.parseInt(attractionId);
            Attraction attraction = session.get(Attraction.class, id);

            if (attraction != null) {
                // 刪除該景點資料
                session.delete(attraction);
                transaction.commit();  // 提交事務
                request.setAttribute("message", "景點資料刪除成功!");
            } else {
                request.setAttribute("message", "找不到指定的景點資料，刪除失敗!");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // 發生錯誤時回滾事務
            }
            e.printStackTrace();
            request.setAttribute("message", "刪除過程中發生錯誤: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();  // 關閉 Hibernate Session
            }
        }

        // 導向結果頁面
        request.getRequestDispatcher("/static/attraction/DeleteAttraction.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}