package com.cloudSerenityHotel.attraction_facility.attraction.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.attraction_facility.attraction.service.PictureService;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class PictureServiceImpl implements PictureService {

    @Override
    public Picture findPictureById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 根據ID查詢圖片
            return session.get(Picture.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Picture> findAllPictures() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 查詢所有圖片
            return session.createQuery("FROM Picture", Picture.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deletePicture(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Picture picture = session.get(Picture.class, id);
            if (picture != null) {
                session.delete(picture); // 刪除圖片
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
    public boolean addPicture(Picture picture) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(picture); // 新增圖片
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
    public List<Picture> findPicturesByReference(int referenceId, String referenceType) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 根據關聯ID和類型查詢圖片
            return session.createQuery("FROM Picture WHERE referenceId = :referenceId AND referenceType = :referenceType", Picture.class)
                          .setParameter("referenceId", referenceId)
                          .setParameter("referenceType", referenceType)
                          .list();
        } finally {
            session.close();
        }
    }
}
