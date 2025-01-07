package com.cloudSerenityHotel.rent.model.api;

public class ResponseModel<T> {

	private StatusEnum status;
	private T data;

	public ResponseModel() {
	}
	
	public ResponseModel(StatusEnum status, T data) {
		this.status = status;
		this.data = data;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
