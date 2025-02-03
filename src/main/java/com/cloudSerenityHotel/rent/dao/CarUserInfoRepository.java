package com.cloudSerenityHotel.rent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.rent.model.CarUserInfo;

@Repository
public interface CarUserInfoRepository extends JpaRepository<CarUserInfo, Integer> {

}
