package com.cloudSerenityHotel.rent.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarDetailsRepository extends JpaRepository<CarDetails, String> {

	 @Query("SELECT c FROM CarDetails c WHERE c.carId = ?1")
	 public Optional<CarDetails> findById(String id);
}
