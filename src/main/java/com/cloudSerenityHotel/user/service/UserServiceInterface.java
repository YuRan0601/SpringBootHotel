package com.cloudSerenityHotel.user.service;

import java.util.List;
import java.util.Map;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;

import jakarta.servlet.http.HttpSession;

public interface UserServiceInterface {
	
	//登入
	User login(String email,String password);
	
	//註冊
	int register(User user, Member member);
	//查詢
	User findUserById(int id);
	List<User> findUserByName(String name);
	List<User> findAllUser();
	
	User findMemberById(int id);
	List<User> findMemberByName(String name);
	List<User> findAllMember();
	
	//修改資料
	int updateUser(User user);
	int updateMember(User user);
	
	
	//註銷帳號
	int deleteUser(int userId);
	//恢復帳號
	int recoverUser(int userId);
	//新增管理員帳號
	int addAdmin(User user);

	Map<String, Object> getUserInfo(HttpSession session);
}
