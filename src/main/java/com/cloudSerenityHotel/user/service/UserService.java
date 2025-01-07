package com.cloudSerenityHotel.user.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.model.UserDao;
import com.cloudSerenityHotel.user.model.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserServiceInterface {
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRepository uRepository;

//	public UserService() {
//		this.userDao = new UserDao();
//	}

	@Override
	public User login(String email, String password) {
		return uRepository.findByEmailAndPassword(email, password).orElse(null);
	}

	@Override
	public int checkEmail(String email) {
		User checkResult = uRepository.findByEmail(email).orElse(null);
		if (checkResult == null) {
			return 1;
		}
		return 0;
	}

	@Override
	public int register(User user, Member member) {
		User checkResult = uRepository.findByEmail(user.getEmail()).orElse(null);
		if (checkResult == null) {
			return 0;
		}
		user.setUserStatus("In_use");
		user.setAccountUpdateTime(LocalDateTime.now());
		member.setRegisterDate(LocalDateTime.now());
		member.setDataUpdateTime(LocalDateTime.now());
		member.setUser(user);
		user.setMember(member);
		uRepository.save(user);
		return 1;
	}

	@Override
	public int deleteUser(int userId) {
		try {
			userDao.removeUser(userId);
			return 1;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	@Override
	public int recoverUser(int userId) {
		try {
			userDao.recoverUser(userId);
			return 1;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	@Override
	public int addAdmin(User user) {
		User checkResult = uRepository.findByEmail(user.getEmail()).orElse(null);
		if (checkResult != null) {
			return 0;
		}
		user.setUserStatus("In_use");
		user.setAccountUpdateTime(LocalDateTime.now());
		user.setMember(null);
		uRepository.save(user);
		return 1;
	}

	@Override
	public User findUserById(int id) {
		return userDao.findAdminById(id);
	}

	@Override
	public List<User> findUserByName(String name) {
		return userDao.findAdminByName(name);
	}

	@Override
	public List<User> findAllUser() {
		return uRepository.findByUserIdentity("admin");
	}

	@Override
	public User findMemberById(int id) {
		return userDao.findMemberById(id);
	}

	@Override
	public List<User> findMemberByName(String name) {
		return userDao.findMemberByName(name);
	}

	@Override
	public List<User> findAllMember() {
		return uRepository.findByUserIdentity("user");
	}

	@Override
	public int updateUser(User user) {
		try {
			userDao.updateUser(user);
			return 1;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	@Override
	public int updateMember(User user) {
		try {
			userDao.updateUser(user);
			userDao.updateMember(user);
			return 1;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	@Override
	public Map<String, Object> getUserInfo(HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			result.put("code", 505); // 505 顯示未登入
			return result;
		}

		User user = userDao.findAdminById(userId);

		if (user == null) {
			result.put("code", 404); // 404 沒有找到user
			return result;
		} else {
			result.put("code", 200); // 200 成功找到
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("userId", user.getUserId());
			userMap.put("userName", user.getUserName());
			userMap.put("userIdentity", user.getUserIdentity());

			result.put("data", userMap);
			return result;
		}
	}

}
