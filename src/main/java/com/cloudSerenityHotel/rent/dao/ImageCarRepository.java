package com.cloudSerenityHotel.rent.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.ImageCar;

@Repository
public interface ImageCarRepository extends JpaRepository<ImageCar, Integer> {
	
	@Query("SELECT imageUrl FROM ImageCar c WHERE c.modelId = :modelId")
	Optional<List<String>> findImageUrlByModelId(Integer modelId);
	
	
	@Query("SELECT c FROM ImageCar c WHERE c.modelId = :modelId")
	List<ImageCar> findByModelId(Integer modelId);
}
