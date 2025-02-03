package com.cloudSerenityHotel.rent.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarDetails;

@Repository
public interface CarDetailsRepository extends JpaRepository<CarDetails, String> {

	@Query("SELECT c FROM CarDetails c WHERE c.carId = ?1")
	public Optional<CarDetails> findById(String id);

	@Query("SELECT c FROM CarDetails c WHERE c.status = :status AND c.carModelId = :carModelId")
	List<CarDetails> findAvailableVehicles(String status, int carModelId);
}
