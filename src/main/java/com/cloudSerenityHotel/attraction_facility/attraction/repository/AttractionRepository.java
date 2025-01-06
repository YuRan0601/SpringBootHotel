package com.cloudSerenityHotel.attraction_facility.attraction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;

@RequestMapping(value = "/api/attractions", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {
}
