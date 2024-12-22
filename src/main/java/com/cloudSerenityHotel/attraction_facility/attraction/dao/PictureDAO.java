package com.cloudSerenityHotel.attraction_facility.attraction.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Picture;
import com.cloudSerenityHotel.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class PictureDAO {

    // 插入圖片資料
    public void insertPicture(Picture picture) {
        // 開啟一個 Hibernate Session
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 開始一個事務
            Transaction transaction = session.beginTransaction();

            // 插入圖片資料
            session.save(picture);

            // 提交事務
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 刪除圖片資料
    public void delete(int imageId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Picture picture = session.get(Picture.class, imageId); // 根據圖片 ID 查找圖片
            if (picture != null) {
                session.delete(picture); // 刪除圖片
            }
            transaction.commit(); // 提交交易
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新圖片資料
    public boolean update(Picture picture) {
        // 開啟一個 Hibernate Session
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 開始一個事務
            Transaction transaction = session.beginTransaction();

            // 使用 Hibernate 更新圖片資料
            session.update(picture);

            // 提交事務
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查詢單一圖片資料 (根據ID)
    public Picture getOne(int imageId) {
        Picture picture = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            picture = session.get(Picture.class, imageId);  // 根據圖片 ID 查找圖片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }

    // 查詢所有圖片資料
    public List<Picture> getAll() {
        List<Picture> pictures = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Picture> query = session.createQuery("FROM Picture", Picture.class);
            pictures = query.list();  // 查詢所有圖片資料
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictures;
    }

    // 根據關聯資料查詢圖片
    public List<Picture> getPicturesByReference(int referenceId, String referenceType) {
        List<Picture> pictures = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Picture WHERE referenceId = :referenceId AND referenceType = :referenceType";
            Query<Picture> query = session.createQuery(hql, Picture.class);
            query.setParameter("referenceId", referenceId);
            query.setParameter("referenceType", referenceType);
            pictures = query.list();  // 查詢指定 referenceId 和 referenceType 的圖片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictures;
    }
    
    // 根據 referenceId 查詢圖片
    public Picture getByReferenceId(String referenceId) {
        Picture picture = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 開啟交易
            Transaction transaction = session.beginTransaction();

            // 使用 HQL 查詢圖片
            Query<Picture> query = session.createQuery("FROM Picture WHERE referenceId = :referenceId", Picture.class);
            query.setParameter("referenceId", referenceId);
            picture = query.uniqueResult();

            // 提交交易
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }
}
