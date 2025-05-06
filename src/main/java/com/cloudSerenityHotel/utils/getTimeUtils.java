package com.cloudSerenityHotel.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class getTimeUtils {
	
	/**
	 * 取得現在的時間 並轉為timestamp格式
	 * 方便放入sql中
	 */
	public Timestamp getNowTime() {
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);
		return timestamp;
	}
}
