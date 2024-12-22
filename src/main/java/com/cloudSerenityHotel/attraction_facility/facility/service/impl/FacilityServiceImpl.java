package com.cloudSerenityHotel.attraction_facility.facility.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.cloudSerenityHotel.attraction_facility.facility.dao.FacilityDAO;
import com.cloudSerenityHotel.attraction_facility.facility.model.Facility;
import com.cloudSerenityHotel.attraction_facility.facility.service.FacilityService;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class FacilityServiceImpl implements FacilityService {

    private FacilityDAO facilityDAO = new FacilityDAO(); // 使用 DAO 來進行資料庫操作

    @Override
    public Facility findFacilityByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 查詢設施資料
            return session.createQuery("FROM Facility WHERE name = :name", Facility.class)
                          .setParameter("name", name)
                          .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Facility> findAllFacilities() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 查詢所有設施資料
            return session.createQuery("FROM Facility", Facility.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteFacility(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Facility facility = session.get(Facility.class, id);
            if (facility != null) {
                session.delete(facility); // 刪除設施
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
    public boolean addFacility(Facility facility) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(facility); // 新增設施
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

    @Override
    public boolean updateFacility(Facility facility) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(facility); // 更新設施資料
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
