package com.cloudSerenityHotel.rent.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarRentalRecord;

@Repository
public interface CarRentalRecordRespostiroy extends JpaRepository<CarRentalRecord, String> {

	@Query(value = "SELECT TOP 50 * FROM CarRentalRecords WHERE car_id = :carId ORDER BY updated_at DESC", nativeQuery = true)
	List<CarRentalRecord> findTop50ByCarIdOrderByUpdatedAt(String carId);

	@Query(value = "SELECT * FROM CarRentalRecords WHERE rental_status = ?1", nativeQuery = true)
	List<CarRentalRecord> findAllByStatus(String status);
}
