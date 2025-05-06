package com.cloudSerenityHotel.attraction_facility.facility.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;
import com.cloudSerenityHotel.utils.HibernateUtil;

import java.util.List;

public class FacilityDAO {
    private SessionFactory sessionFactory;

    public FacilityDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();  // HibernateUtil 是你管理 SessionFactory 的類
    }

    // 查詢單一設施資料 (根據ID)
    public Facility getOne(int facilityId) {
        Facility facility = null;
        try (Session session = sessionFactory.openSession()) {
            facility = session.get(Facility.class, facilityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facility;
    }

    // 查詢單一設施資料 (根據名稱)
    // 根據名稱查找設施
    public Facility getOneByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Facility facility = null;
        
        try {
            transaction = session.beginTransaction();
            
            // 使用 HQL 查詢設施資料
            String hql = "FROM Facility WHERE name = :name";
            Query<Facility> query = session.createQuery(hql, Facility.class);
            query.setParameter("name", name);
            
            // 取得查詢結果
            facility = query.uniqueResult();
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return facility;
    }

    // 查詢所有設施資料
    public List<Facility> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Facility> facilityList = null;

        try {
            session.beginTransaction();
            facilityList = session.createQuery("FROM Facility", Facility.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return facilityList;
    }

    // 插入設施資料
    public void insertFacility(Facility facility) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(facility);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    // 刪除設施資料
    public void delete(int facilityId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Facility facility = session.get(Facility.class, facilityId);
            if (facility != null) {
                session.delete(facility);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 更新設施資料
    public boolean update(Facility facility) {
        boolean isSuccess = false;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(facility);
            transaction.commit();
            isSuccess = true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return isSuccess;
    }
    
    public Facility getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Facility facility = null;

        try {
            session.beginTransaction();

            // Hibernate 查詢，通過名稱查詢設施資料
            Query<Facility> query = session.createQuery("FROM Facility WHERE name = :name", Facility.class);
            query.setParameter("name", name);
            facility = query.uniqueResult(); // 如果找到匹配的結果，返回唯一結果

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return facility;
    }
}
