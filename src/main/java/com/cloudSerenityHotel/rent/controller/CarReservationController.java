package com.cloudSerenityHotel.rent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.rent.model.CarReservationCreateRequest;
import com.cloudSerenityHotel.rent.model.CarReservationStatuEnum;
import com.cloudSerenityHotel.rent.service.CarReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE })
@RequestMapping(path = "/car-reservation")
public class CarReservationController {

	private final CarReservationService carReservationService;

	// 新增訂單
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody CarReservationCreateRequest carReservationCreateRequest) {
		return ResponseEntity.ok(carReservationService.create(carReservationCreateRequest));
	}

	// 查詢單筆訂單 使用預約編號
	@GetMapping("/query/{reservationId}")
	public ResponseEntity<?> query(@PathVariable String reservationId) {
		return ResponseEntity.ok(carReservationService.qetUserInfoAndRentalRecord(reservationId));
	}
	
	// 查詢單筆訂單 使用預約編號
		@GetMapping("/query/list/{reservationId}")
		public ResponseEntity<?> qetRentalRecordByIdList(@PathVariable String reservationId) {
			return ResponseEntity.ok(carReservationService.qetRentalRecordByIdList(reservationId));
		}
	
	// 查詢會員租借中訂單
	@GetMapping("/query/rented/{userId}")
	public ResponseEntity<?> queryAllRented(@PathVariable String userId) {
		return ResponseEntity.ok(carReservationService.queryByStatusAndUserId(CarReservationStatuEnum.RENTED, userId));
	}
	
	// 查詢會員的所有訂單
		@GetMapping("/query/user/{userId}")
		public ResponseEntity<?> queryAllRentedByCustomerId(@PathVariable int userId) {
			return ResponseEntity.ok(carReservationService.queryByUserId(userId));
		}
	
	// 查詢所有租借中訂單
	@GetMapping("/query-all/rented")
	public ResponseEntity<?> queryAllRented() {
		return ResponseEntity.ok(carReservationService.queryAllByStatus(CarReservationStatuEnum.RENTED));
	}
	
	// 查詢所有已預約訂單
	@GetMapping("/query-all/reserved")
	public ResponseEntity<?> queryAllReserved() {
		return ResponseEntity.ok(carReservationService.queryAllByStatus(CarReservationStatuEnum.RESERVED));
	}

	// 更新訂單狀態至租借中
	@PostMapping("/update/rented/{reservationId}")
	public ResponseEntity<?> updateStatusToRented(@PathVariable String reservationId) {
		return ResponseEntity.ok(carReservationService.updateStatus(reservationId, CarReservationStatuEnum.RENTED));
	}

	// 更新訂單狀態至尚未預約
	@PostMapping("/update/available/{reservationId}")
	public ResponseEntity<?> updateStatusToAvailable(@PathVariable String reservationId) {
		return ResponseEntity.ok(carReservationService.updateStatus(reservationId, CarReservationStatuEnum.AVAILABLE));
	}

	// 刪除訂單
	@PostMapping("/delete/{reservationId}")
	public ResponseEntity<?> delete(@PathVariable String reservationId) {
		return ResponseEntity.ok(carReservationService.delete(reservationId, CarReservationStatuEnum.AVAILABLE));
	}

}
