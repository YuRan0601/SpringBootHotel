package com.cloudSerenityHotel.rent.controller;

import java.util.List;
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

	@PostMapping(path = "/deleta")
	@ResponseBody
	public void carDetailsActionDelete(@RequestBody CarDetails carDetails) {
		carDetailsService.deleteCarDetails(carDetails);
	}

	@GetMapping(path = "/queryAll")
	@ResponseBody
	public ResponseEntity<?> carDetailsActionAll() {
		ResponseModel resp = carDetailsService.findAll();
		return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
	}

	@GetMapping("/available-vehicles")
	@ResponseBody
	public ResponseEntity<?> getAvailableVehicles() {
		// 調用服務層來查詢可租用的車輛
		ResponseModel<List<CarDetails>> response = carDetailsService.findAvailableVehicles();

		// 檢查查詢結果的狀態
		if (response != null && response.getStatus() == StatusEnum.SUCCESS) {
			// 查詢成功，返回車輛列表
			return ResponseEntity.ok(response.getData());
		} else {
			// 查詢失敗，返回錯誤訊息
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(response != null ? response.getData() : "未找到可租用的車輛");
		}

	}
}
