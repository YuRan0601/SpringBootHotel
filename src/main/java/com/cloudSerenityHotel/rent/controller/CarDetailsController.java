package com.cloudSerenityHotel.rent.controller;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudSerenityHotel.rent.model.CarDetails;
import com.cloudSerenityHotel.rent.model.CarDetailsService;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

@Controller
@CrossOrigin(origins = { "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE })
@RequestMapping(path = "/CarDetails")
public class CarDetailsController {

	@Autowired
	private CarDetailsService carDetailsService;

	@GetMapping(path = "/queryOne/{carId}")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionQuery(@PathVariable("carId") String carId) {
		if (StringUtils.isNotBlank(carId)) {
			ResponseModel resp = carDetailsService.findById(carId);
			return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無id");
	}

	@PostMapping(path = "/add")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionAdd(@RequestBody CarDetails carDetails) {
		if (Objects.nonNull(carDetails)) {
			ResponseModel resp = carDetailsService.insertCarDetails(carDetails);
			return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新增失敗，請提供有效的車輛資料");
	}

	@PostMapping(path = "/update")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionUpdate(@RequestBody CarDetails carDetails) {
		if (Objects.nonNull(carDetails)) {
			ResponseModel resp = carDetailsService.updateCarDetails(carDetails);
			return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("修改失敗，請提供有效的車輛資料");
	}

	@PostMapping(path = "/delete")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionDelete(@RequestBody CarDetails carDetails) {
	    // 檢查是否提供有效的 carDetails 物件
	    if (Objects.nonNull(carDetails)) {
	        try {
	            // 刪除車輛詳情
	            carDetailsService.deleteCarDetails(carDetails);
	            // 刪除成功返回 200 OK 並顯示成功訊息
	            return ResponseEntity.ok("刪除成功");
	        } catch (Exception e) {
	            // 捕獲異常並返回 500 錯誤狀態和錯誤訊息
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除失敗，請稍後再試");
	        }
	    }
	    // 如果提供的 carDetails 無效，返回 400 錯誤狀態和錯誤訊息
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("請提供有效的車輛資料");
	}

	@GetMapping(path = "/queryAll")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionAll() {
		ResponseModel resp = carDetailsService.findAll();
		return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
	}

	@GetMapping("/available-vehicles/{carModelId}")
	@ResponseBody
	public ResponseEntity<?> getAvailableVehicles(@PathVariable Integer carModelId) {
		return ResponseEntity.ok(carDetailsService.findAvailableVehicle(carModelId));
	}
	
}
