package com.cloudSerenityHotel.rent.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cloudSerenityHotel.rent.dao.CarDetailsRepository;
import com.cloudSerenityHotel.rent.dao.CarModelRepository;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;
import com.cloudSerenityHotel.rent.model.utils.TimeProvider;

import jakarta.transaction.Transactional;

@Service
public class CarDetailsService {

	@Autowired
	private CarDetailsRepository carDetailsRepository;

	@Autowired
	private CarModelRepository carModelRepository;

	@Autowired
	private TimeProvider timeProvider;

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
		try {
			// 取得當前時間
			LocalDateTime currentTime = timeProvider.getCurrentTime();
			carDetails.setUpdatedAt(currentTime);
			carDetails.setCreatedAt(currentTime);
			System.out.println(CarReservationStatuEnum.AVAILABLE.name());
			carDetails.setStatus(CarReservationStatuEnum.AVAILABLE.name());

			// 保存車輛詳情
			CarDetails carDetailsResp = carDetailsRepository.save(carDetails);

			if (carDetailsResp != null) {
				// 根據車型 ID 查詢車型表中的 totalVehicles 欄位
				Integer carModelId = carDetails.getCarModelId(); // 修正變數名稱為 carModelId
				CarModel carModel = carModelRepository.findById(carModelId).orElse(null);

				if (carModel != null) {
					// 如果 carModel 的 totalVehicles 為 null，則設為 0，否則加 1
					int newTotalVehicles = (carModel.getTotalVehicles() == null ? 0 : carModel.getTotalVehicles()) + 1;

					// 設定新的車輛數量
					carModel.setTotalVehicles(newTotalVehicles);

					// 保存更新後的車型資訊
					carModelRepository.save(carModel);
				}

				// 返回成功結果
				return new ResponseModel<CarDetails>(StatusEnum.SUCCESS, carDetailsResp);
			}

			// 返回新增失敗結果
			return new ResponseModel<String>(StatusEnum.FAIL, "新增失敗");

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModel<String>(StatusEnum.FAIL, "系統錯誤");
		}
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
		try {
			// 先從資料庫中刪除 carDetails
			carDetailsRepository.delete(carDetails);

			// 根據 carModelId 更新對應車型資料中的 totalVehicles
			Integer carModelId = carDetails.getCarModelId();
			CarModel carModel = carModelRepository.findById(carModelId).orElse(null);

			if (carModel != null) {
				// 進行 totalVehicles 減 1 操作
				int newTotalVehicles = (carModel.getTotalVehicles() == null ? 0 : carModel.getTotalVehicles()) - 1;
				if (newTotalVehicles < 0) {
					newTotalVehicles = 0;
				}

				// 更新 carModel 的 totalVehicles
				carModel.setTotalVehicles(newTotalVehicles);
				carModelRepository.save(carModel);
			}
		} catch (Exception e) {
			throw new RuntimeException("刪除車輛失敗", e); // 可以根據需要處理異常
		}
	}

	public ResponseModel<?> findAvailableVehicle(int carModelId) {
		List<CarDetails> list = carDetailsRepository.findAvailableVehicles(CarReservationStatuEnum.AVAILABLE.name(), carModelId);

		Optional<CarDetails> carDetailsOpt = list.stream().findFirst();
		if (carDetailsOpt.isPresent()) {
			return new ResponseModel<>(StatusEnum.SUCCESS, carDetailsOpt.get());
		}

		return new ResponseModel<>(StatusEnum.FAIL, "未找到可租用的車輛");
	}
}
