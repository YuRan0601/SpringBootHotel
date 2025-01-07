package com.cloudSerenityHotel.rent.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CarModelService {

	@Autowired
	private CarModelRepository carModelRepository;
	
	public CarModel findById(int id) {
		Optional<CarModel> carModelResp = carModelRepository.findById(id);
		return carModelResp.orElse(null);
	}
	
	public List<CarModel> findAll(){
		return carModelRepository.findAll();
	}
	
	public Page<CarModel> findAllByPage(Pageable pageable){
		return carModelRepository.findAll(pageable);
	}
	
	@Transactional
	public CarModel insertCarModel(CarModel carModel) {
		return carModelRepository.save(carModel);
	}
	
	@Transactional
	public CarModel updateCarModel(CarModel carModel) {
		return carModelRepository.save(carModel);
	}
	
	@Transactional
	public boolean  deleteCarModel(int id) {
		 Optional<CarModel> carModel = carModelRepository.findById(id);
		 if (carModel.isPresent()) {
	            carModelRepository.deleteById(id);  // 刪除
	            return true;  // 成功刪除
	        } else {
	            return false;  // 車型不存在
	        }
	}
}
