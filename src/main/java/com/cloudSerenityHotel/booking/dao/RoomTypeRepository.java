package com.cloudSerenityHotel.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.booking.model.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

}
