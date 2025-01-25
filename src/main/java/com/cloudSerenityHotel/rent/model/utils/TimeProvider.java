package com.cloudSerenityHotel.rent.model.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

@Repository
public class TimeProvider {

	public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now(); // 獲取當前時間
	}
}
