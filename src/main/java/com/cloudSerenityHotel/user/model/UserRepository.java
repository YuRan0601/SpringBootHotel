package com.cloudSerenityHotel.user.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmailAndPassword(String email, String password);

	Optional<User> findByEmail(String email);
	
	List<User> findByUserIdentity(String userIdentity);
}
