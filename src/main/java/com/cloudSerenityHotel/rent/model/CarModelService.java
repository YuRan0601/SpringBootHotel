package com.cloudSerenityHotel.rent.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.rent.dao.CarModelRepository;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;
import com.cloudSerenityHotel.rent.model.utils.TimeProvider;

import jakarta.transaction.Transactional;

@Service
public class CarModelService {

	@Autowired
	private CarModelRepository carModelRepository;
	
	@Autowired
	private TimeProvider timeProvider;

	
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
		 LocalDateTime currentTime = timeProvider.getCurrentTime();
		 carModel.setUpdatedAt(currentTime);
		 carModel.setCreatedAt(currentTime);
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
	
	public ResponseModel countByCarModel(Integer carModelId) {
	    Integer vehicleCount = carModelRepository.countByCarModel(carModelId);
	    
	    // 如果查詢結果為 null，則將 totalVehicles 設為 0
	    if (vehicleCount == null) {
	        vehicleCount = 1;
	    }
	    return new ResponseModel<>(StatusEnum.SUCCESS, vehicleCount);
	}
	
}
