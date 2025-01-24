package com.cloudSerenityHotel.rent.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarDetails;
import com.cloudSerenityHotel.rent.model.CarModel;

@Repository
public interface CarDetailsRepository extends JpaRepository<CarDetails, String> {

	@Query("SELECT c FROM CarDetails c WHERE c.carId = ?1")
	public Optional<CarDetails> findById(String id);

	@Query("SELECT c FROM CarDetails c WHERE c.status = '可租用'")
	List<CarDetails> findAvailableVehicles();
}
