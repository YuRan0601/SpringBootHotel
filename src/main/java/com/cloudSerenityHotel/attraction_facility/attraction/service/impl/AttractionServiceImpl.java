package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class AttractionServiceImpl implements AttractionService {

    @Override
    public Attraction findAttractionByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 查詢景點資料
            return session.createQuery("FROM AttractionBean WHERE name = :name", Attraction.class)
                          .setParameter("name", name)
                          .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Attraction> findAllAttractions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 查詢所有景點資料
            return session.createQuery("FROM AttractionBean", Attraction.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteAttraction(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Attraction attraction = session.get(Attraction.class, id);
            if (attraction != null) {
                session.delete(attraction); // 刪除景點
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean addAttraction(Attraction attraction) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(attraction); // 新增景點
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
