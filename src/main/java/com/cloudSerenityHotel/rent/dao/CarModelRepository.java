package com.cloudSerenityHotel.rent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarModel;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Integer>{

}
