package com.cloudSerenityHotel.rent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudSerenityHotel.rent.model.CarModel;
import com.cloudSerenityHotel.rent.model.CarModelService;


@Controller
@RequestMapping(path = "/CarModel")
public class CarModelController {

	@Autowired
	private CarModelService carModelService;

	@GetMapping(path = "/queryOne/{carModelId}")
	@ResponseBody
	public CarModel carModelActionQuery(@PathVariable("carModelId") int id) {
		 return  carModelService.findById(id);
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
		boolean isDeleted = carModelService.deleteCarModel(carModel.getCarId());
		if (isDeleted) {
            return ResponseEntity.ok("Car model deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car model not found.");
        }
	}
	
	@GetMapping(path = "/queryAll")
	@ResponseBody
	public List<CarModel> carModelActionAll(){
		return carModelService.findAll();
	}
}
