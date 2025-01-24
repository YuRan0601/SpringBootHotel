package com.cloudSerenityHotel.rent.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudSerenityHotel.rent.dao.CarDetailsRepository;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

import jakarta.transaction.Transactional;

@Service
public class CarDetailsService {

	@Autowired
	private CarDetailsRepository carDetailsRepository;

	public ResponseModel findById(String id) {
		Optional<CarDetails> carDetailsResp = carDetailsRepository.findById(id);
		if (carDetailsResp.isPresent()) {
			return new ResponseModel<CarDetails>(StatusEnum.SUCCESS, carDetailsResp.get());
		}
		return new ResponseModel<String>(StatusEnum.FAIL, "查無ID");
	}

	public ResponseModel findAll() {
		List<CarDetails> carDetailsResp = carDetailsRepository.findAll();
		if (!CollectionUtils.isEmpty(carDetailsResp)) {
			return new ResponseModel<List<CarDetails>>(StatusEnum.SUCCESS, carDetailsResp);
		}
		return new ResponseModel<String>(StatusEnum.FAIL, "查無資料");
	}

	public ResponseModel findAllByPage(Pageable pageable) {
		Page<CarDetails> carDetailsRespPageAll = carDetailsRepository.findAll(pageable);
		if (carDetailsRespPageAll.isEmpty()) {
			return new ResponseModel<Page<CarDetails>>(StatusEnum.SUCCESS, carDetailsRespPageAll);
		}
		return new ResponseModel<String>(StatusEnum.FAIL, "查無資料");
	}

	@Transactional
	public ResponseModel insertCarDetails(CarDetails carDetails) {
		CarDetails carDetailsResp = carDetailsRepository.save(carDetails);
		if (carDetailsResp != null) {
			return new ResponseModel<CarDetails>(StatusEnum.SUCCESS, carDetailsResp);
		}
		return new ResponseModel<String>(StatusEnum.FAIL, "新增失敗");
	}

	@Transactional
	public ResponseModel updateCarDetails(CarDetails carDetails) {
		CarDetails carDetailsResp = carDetailsRepository.save(carDetails);
		if (carDetailsResp != null) {
			return new ResponseModel<CarDetails>(StatusEnum.SUCCESS, carDetailsResp);
		}
		return new ResponseModel<String>(StatusEnum.FAIL, "失敗");
	}

	@Transactional
	public void deleteCarDetails(CarDetails carDetails) {
		carDetailsRepository.delete(carDetails);
	}
	
	public ResponseModel<List<CarDetails>> findAvailableVehicles()  {
		List<CarDetails> carDetailsResp = carDetailsRepository.findAvailableVehicles();
		if(carDetailsResp != null && !carDetailsResp.isEmpty()) {
			return new ResponseModel<>(StatusEnum.SUCCESS, carDetailsResp);
		}
		 return new ResponseModel(StatusEnum.FAIL, "未找到可租用的車輛");
	}
}
