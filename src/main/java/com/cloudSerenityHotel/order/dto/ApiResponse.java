package com.cloudSerenityHotel.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * 統一的 API 回應類
 * @param <T> 泛型類型，適用於返回任意數據
 */
@Getter @Setter 
@AllArgsConstructor 
@NoArgsConstructor
public class ApiResponse<T> {
	//返回錯誤訊息時，使用一個自定義的 DTO 類
	private String message;  // 返回的訊息
	private boolean success; // 是否成功
    private T data;          // 泛型數據內容

 // 如果 @AllArgsConstructor 不適用，也可以明確定義建構函數
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
