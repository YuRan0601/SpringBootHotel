package com.cloudSerenityHotel.attraction_facility.attraction.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class AttractionDAO {

    // 新增景點的方法
    public void addAttraction(Attraction attraction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(attraction);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查詢所有景點的方法
    public List<Attraction> getAllAttractions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Attraction", Attraction.class).list();
        }
    }

    // 更新景點的方法
    public boolean updateAttraction(Attraction attraction) {
        boolean success = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(attraction);  // 使用 Hibernate 的 update 方法
            transaction.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // 刪除景點的方法
    public void deleteAttraction(int attractionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Attraction attraction = session.get(Attraction.class, attractionId);
            if (attraction != null) {
                session.delete(attraction);
            }
            transaction.commit();
        }
    }
    
    public Attraction getOneByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attraction> query = session.createQuery("FROM Attraction WHERE name = :name", Attraction.class);
            query.setParameter("name", name);
            return query.uniqueResult();  // 返回單一景點資料
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}