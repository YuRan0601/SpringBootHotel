package com.cloudSerenityHotel.rent.controller;

import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudSerenityHotel.rent.model.CarModel;
import com.cloudSerenityHotel.rent.model.CarModelService;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

@Controller
@CrossOrigin(origins = { "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE })
@RequestMapping(path = "/CarModel")
public class CarModelController {
	@Autowired
	private CarModelService carModelService;

	@GetMapping(path = "/queryOne/{carModelId}")
	@ResponseBody
	public CarModel carModelActionQuery(@PathVariable("carModelId") int id) {
		return carModelService.findById(id);
	}

	@PostMapping(path = "/add")
	@ResponseBody
	public CarModel carModelActionAdd(@RequestBody CarModel carModel) {
		return carModelService.insertCarModel(carModel);
	}

	@PostMapping(path = "/update")
	@ResponseBody
	public CarModel carModelActionUpdate(@RequestBody CarModel carModel) {
		return carModelService.updateCarModel(carModel);
	}

	@PostMapping(path = "/delete")
	public ResponseEntity<String> carModelActionDelete(@RequestBody CarModel carModel) {
		boolean isDeleted = carModelService.deleteCarModel(carModel.getCarModelId());
		if (isDeleted) {
			return ResponseEntity.ok("Car model deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car model not found.");
		}
	}

	@GetMapping(path = "/queryAll")
	@ResponseBody
	public List<CarModel> carModelActionAll() {
		return carModelService.findAll();
	}

	@GetMapping("/countByCarModel")
	@ResponseBody
	public ResponseEntity<?> countByCarModel(@RequestParam String carModel) {
		if (Objects.nonNull(carModel) && !carModel.isEmpty()) {
			ResponseModel<Long> resp = carModelService.countByCarModel(carModel);

			// 根據查詢結果的狀態返回相應的 HTTP 狀態碼
			return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
					: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("查詢失敗，請提供有效的車型名稱");
	}

	
}
