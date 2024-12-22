package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/InsertAttraction")
public class InsertAttraction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 取得來自表單的資料
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String openingHours = request.getParameter("openingHours");
        String contactInfo = request.getParameter("contactInfo");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String imageUrl = request.getParameter("imageUrl");

        // 創建 Attraction 物件並設置其屬性
        Attraction attraction = new Attraction();
        attraction.setName(name);
        attraction.setDescription(description);
        attraction.setLocation(location);
        attraction.setOpeningHours(openingHours);
        attraction.setContactInfo(contactInfo);
        attraction.setTypeId(typeId);
        attraction.setImageUrl(imageUrl);

        // 設置創建和更新日期
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        attraction.setCreateAt(currentTimestamp);
        attraction.setUpdateAt(currentTimestamp);

        // 使用 Hibernate 來插入資料
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // 使用 session.save() 方法來插入資料
            session.save(attraction);

            // 提交事務
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // 發生錯誤時回滾事務
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();  // 關閉 session
            }
        }

        // 重定向到展示所有景點的頁面
        response.sendRedirect("GetAllAttractions");
    }
}
