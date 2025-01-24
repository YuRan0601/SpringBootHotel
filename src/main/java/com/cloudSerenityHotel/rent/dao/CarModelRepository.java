package com.cloudSerenityHotel.rent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarModel;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Integer>{

	 @Query("SELECT c.totalVehicles FROM CarModel c WHERE c.carModel = :carModel")
	Integer countByCarModel(@Param("carModel") String carModel);

}
