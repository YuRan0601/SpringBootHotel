package com.cloudSerenityHotel.attraction_facility.attraction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {
}
