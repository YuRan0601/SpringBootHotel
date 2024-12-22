package com.cloudSerenityHotel.user.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.utils.HibernateUtil;

public class UserDao {

	private SessionFactory factory;
	
	public UserDao() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	//登入
	public User login(String email,String pwd) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
		session.createQuery("from User u where u.email = :e and u.password = :p",User.class);
		query.setParameter("e", email);
		query.setParameter("p", pwd);
		
		try {
			User result = query.getSingleResult();
			return result;
		} catch (Exception e) {
//			System.out.println("帳號密碼錯誤");
			return null;
		}
	}
	
	//註冊
	//檢查email
	public User checkEmail(User user) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
		session.createQuery("from User u where u.email = :e",User.class);
		query.setParameter("e", user.getEmail());
		
		try {
			User result = query.getSingleResult();
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	//新增會員資料
	public User addMemeber(User user,Member member) {
		Session session = factory.getCurrentSession();
		user.setAccountUpdateTime(LocalDateTime.now());
		member.setRegisterDate(LocalDateTime.now());
		member.setDataUpdateTime(LocalDateTime.now());
		member.setUser(user);
		user.setMember(member);
		session.persist(user);
		
		return user;
	}
	
	
	//查詢
	//admin
	public User findAdminById(int userid) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
		session.createQuery("from User u where u.userId = :id and userIdentity = 'admin'",User.class);
		query.setParameter("id", userid);
		
		try {
			User result = query.getSingleResult();
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<User> findAdminByName(String name) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
				session.createQuery("from User u where u.userName like :n and userIdentity = 'admin'",User.class);
		query.setParameter("n", "%" + name + "%");
		
		return query.list();
	}
	
	public List<User> findAllAdmin() {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
				session.createQuery("from User where userIdentity = 'admin'",User.class);
		return query.list();
	}
	
	//memeber
	public User findMemberById(int userid) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
				session.createQuery("from User u where u.userId = :id and userIdentity = 'user'",User.class);
		query.setParameter("id", userid);
		
		try {
			User result = query.getSingleResult();
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<User> findMemberByName(String name) {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
				session.createQuery("from User u where u.userName like :n and userIdentity = 'user'",User.class);
		query.setParameter("n", "%" + name + "%" );
		
		return query.list();
	}
	
	public List<User> findAllMember() {
		Session session = factory.getCurrentSession();
		
		Query<User> query = 
				session.createQuery("from User where userIdentity = 'user'",User.class);
		return query.list();
	}
	
	//修改帳號資料
	public void updateUser(User user) {
		Session session = factory.getCurrentSession();
		User oldData = session.find(User.class, user.getUserId());
		oldData.setEmail(user.getEmail());
		oldData.setPassword(user.getPassword());
		oldData.setUserName(user.getUserName());
		oldData.setAccountUpdateTime(LocalDateTime.now());
	}
	//修改會員資料
	public void updateMember(User user) {
		Session session = factory.getCurrentSession();
		Member updataData = user.getMember();
		User oldDataUser = session.find(User.class, user.getUserId());
		Member oldData = oldDataUser.getMember();
		
		oldData.setGender(updataData.getGender());
		oldData.setBirthday(updataData.getBirthday());
		oldData.setPhone(updataData.getPhone());
		oldData.setPersonalIdNo(updataData.getPersonalIdNo());
		oldData.setCountry(updataData.getCountry());
		oldData.setAddress(updataData.getAddress());
		oldData.setPassportNo(updataData.getPassportNo());
		oldData.setDataUpdateTime(LocalDateTime.now());
	}
	
	//註銷帳號
	public void removeUser(Integer userid) {
		Session session = factory.getCurrentSession();
		User user = session.find(User.class, userid);
		user.setUserStatus("Logged_out");
		user.setAccountUpdateTime(LocalDateTime.now());
		
	}
	
	//恢復帳號
	public void recoverUser(Integer userid) {
		Session session = factory.getCurrentSession();
		User user = session.find(User.class, userid);
		user.setUserStatus("In_use");
		user.setAccountUpdateTime(LocalDateTime.now());
		
	}
	
	//新增管理員帳號
	public User addAdmin(User user) {
		Session session = factory.getCurrentSession();
		user.setAccountUpdateTime(LocalDateTime.now());
		user.setMember(null);
		session.persist(user);
		
		return user;
	}
}
