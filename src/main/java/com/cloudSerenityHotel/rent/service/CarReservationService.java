package com.cloudSerenityHotel.rent.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.rent.dao.CarDetailsRepository;
import com.cloudSerenityHotel.rent.dao.CarModelRepository;
import com.cloudSerenityHotel.rent.dao.CarRentalRecordRespostiroy;
import com.cloudSerenityHotel.rent.dao.CarUserInfoRepository;
import com.cloudSerenityHotel.rent.model.CarDetails;
import com.cloudSerenityHotel.rent.model.CarModel;
import com.cloudSerenityHotel.rent.model.CarRentalRecord;
import com.cloudSerenityHotel.rent.model.CarReservationCreateRequest;
import com.cloudSerenityHotel.rent.model.CarReservationQueryResponse;
import com.cloudSerenityHotel.rent.model.CarReservationStatuEnum;
import com.cloudSerenityHotel.rent.model.CarUserInfo;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarReservationService {

	private final CarRentalRecordRespostiroy carRentalRecordRepository;
	private final CarUserInfoRepository carUserInfoRepository;
	private final CarDetailsRepository carDetailsRepository;
	private final CarModelRepository carModelRepository;

	// 用於自增的計數器
	private static int counter = 0;

	public ResponseModel<?> create(CarReservationCreateRequest carReservationCreateRequest) {
		if (carReservationCreateRequest.getRentalEnd().isBefore(carReservationCreateRequest.getRentalStart())) {
			return new ResponseModel<>(StatusEnum.FAIL, "租借起始日期不得晚於結束日期");
		}

		if (carReservationCreateRequest.getRentalStart().isEqual(carReservationCreateRequest.getRentalEnd())) {
			return new ResponseModel<>(StatusEnum.FAIL, "租借起始日期不得等於結束日期");
		}

		Optional<CarDetails> carDetailsOpt = carDetailsRepository.findById(carReservationCreateRequest.getCarId());
		CarDetails carDetails;
		CarModel carModel;
		String status = CarReservationStatuEnum.RESERVED.name();
		if (carDetailsOpt.isPresent()) {
			List<CarRentalRecord> list = carRentalRecordRepository
					.findTop50ByCarIdOrderByUpdatedAt(carReservationCreateRequest.getCarId());
			for (CarRentalRecord carRentalRecord : list) {
				if (compareRetnalDate(carReservationCreateRequest.getRentalStart(),
						carReservationCreateRequest.getRentalEnd(), carRentalRecord.getRentalStart(),
						carRentalRecord.getRentalEnd())) {
					return new ResponseModel<>(StatusEnum.FAIL, "該車輛此時段已被租借，請重新選擇日期時間");
				}
			}

			carDetails = carDetailsOpt.get();
			carDetails.setStatus(status);

			Optional<CarModel> carModelOpt = carModelRepository.findById(carDetails.getCarModelId());
			if (!carModelOpt.isPresent()) {
				return new ResponseModel<>(StatusEnum.FAIL, "找不到該車型資料，請另選其他車型");
			}

			carModel = carModelOpt.get();
		} else {
			return new ResponseModel<>(StatusEnum.FAIL, "查無車輛，請重新選擇車型");
		}

		CarUserInfo carUserInfo = carReservationCreateRequest.getCarUserInfo();

		LocalDateTime dateTime = LocalDateTime.now();
		CarRentalRecord carRentalRecord = new CarRentalRecord();
		carRentalRecord.setId(generateReservationId());
		carRentalRecord.setCarId(carReservationCreateRequest.getCarId());
		carRentalRecord.setCustomerId(carReservationCreateRequest.getCarUserInfo().getUserId());
		carRentalRecord.setRentalStatus(status);
		carRentalRecord.setRentalType(carModel.getCarType());
		carRentalRecord.setRentalSize(carModel.getCarSize());
		carRentalRecord.setRentalStart(carReservationCreateRequest.getRentalStart());
		carRentalRecord.setRentalEnd(carReservationCreateRequest.getRentalEnd());
		carRentalRecord.setCreatedAt(dateTime);
		carRentalRecord.setUpdatedAt(dateTime);

		save(carDetails, carUserInfo, carRentalRecord);

		return new ResponseModel<>(StatusEnum.SUCCESS, "新增訂單成功");
	}

	public ResponseModel<?> updateStatus(String reservationId, CarReservationStatuEnum carReservationStatuEnum) {
		Optional<CarRentalRecord> carRentalRecordOpt = carRentalRecordRepository.findById(reservationId);
		if (!carRentalRecordOpt.isPresent()) {
			return new ResponseModel<>(StatusEnum.FAIL, "查無此訂單");
		}

		LocalDateTime dateTime = LocalDateTime.now();
		CarRentalRecord carRentalRecord = carRentalRecordOpt.get();
		Optional<CarDetails> carDetailsOpt = carDetailsRepository.findById(carRentalRecord.getCarId());
		// 為防止意外發生 查無資料庫車型時僅記錄 不做處理，讓訂單可順利修改狀態 以便結案
		if (!carDetailsOpt.isPresent()) {
			System.out.println("無此車型，詳細請查閱資料庫 表: CarDetails, 對應ID為: " + carRentalRecord.getCarId());
		} else {
			CarDetails carDetails = carDetailsOpt.get();
			carDetails.setStatus(carReservationStatuEnum.name());
			carDetails.setUpdatedAt(dateTime);
			carDetailsRepository.save(carDetails);
		}

		carRentalRecord.setRentalStatus(carReservationStatuEnum.name());
		carRentalRecord.setUpdatedAt(dateTime);
		carRentalRecordRepository.save(carRentalRecord);

		return switch (carReservationStatuEnum) {
		case RENTED -> new ResponseModel<>(StatusEnum.SUCCESS, "取車成功");
		case AVAILABLE -> new ResponseModel<>(StatusEnum.SUCCESS, "還車成功");
		default -> new ResponseModel<>(StatusEnum.FAIL, "查無對應狀態");
		};

	}

	public ResponseModel<?> qetUserInfoAndRentalRecord(String reservationId) {
		Optional<CarRentalRecord> carRentalRecordOpt = carRentalRecordRepository.findById(reservationId);
		if (carRentalRecordOpt.isPresent()) {
			CarRentalRecord carRentalRecord = carRentalRecordOpt.get();

			CarUserInfo carUserInfo = carUserInfoRepository.findById(carRentalRecord.getCustomerId())
					.orElse(new CarUserInfo());

			return new ResponseModel<>(StatusEnum.SUCCESS,
					new CarReservationQueryResponse(carRentalRecord, carUserInfo));
		}

		return new ResponseModel<String>(StatusEnum.FAIL, "查無此筆訂單");
	}
	
	public ResponseModel<?> qetRentalRecordByIdList(String reservationId) {
		Optional<CarRentalRecord> carRentalRecordOpt = carRentalRecordRepository.findById(reservationId);
		if (carRentalRecordOpt.isPresent()) {
			
			ArrayList<CarRentalRecord> res = new ArrayList<CarRentalRecord>();
			
			CarRentalRecord carRentalRecord = carRentalRecordOpt.get();
			
			res.add(carRentalRecord);

			return new ResponseModel<>(StatusEnum.SUCCESS,
					res);
		}

		return new ResponseModel<String>(StatusEnum.FAIL, "查無此筆訂單");
	}

	public ResponseModel<?> queryByStatusAndUserId(CarReservationStatuEnum carReservationStatuEnum, String userId) {
		return new ResponseModel<>(StatusEnum.SUCCESS,
				carRentalRecordRepository.findByStatusAndCustomerId(carReservationStatuEnum.name(), userId));
	}

	public ResponseModel<?> queryByUserId(int userId) {
		return new ResponseModel<>(StatusEnum.SUCCESS, carRentalRecordRepository.findByCustomerId(userId));
	}

	public ResponseModel<?> queryAllByStatus(CarReservationStatuEnum carReservationStatuEnum) {
		return new ResponseModel<>(StatusEnum.SUCCESS,
				carRentalRecordRepository.findAllByStatus(carReservationStatuEnum.name()));
	}
	
	public ResponseModel<?> queryAllCarTape(CarReservationStatuEnum carReservationStatuEnum) {
		return new ResponseModel<>(StatusEnum.SUCCESS,
				carRentalRecordRepository.findAllByStatus(carReservationStatuEnum.name()));
	}

	public ResponseModel<?> delete(String reservationId,CarReservationStatuEnum carReservationStatuEnum) {
		Optional<CarRentalRecord> carRentalRecordOpt = carRentalRecordRepository.findById(reservationId);
		if (!carRentalRecordOpt.isPresent()) {
			return new ResponseModel<>(StatusEnum.FAIL,"查無訂單");
		}
		LocalDateTime deteTime = LocalDateTime.now();
		CarRentalRecord carRentalRecord = carRentalRecordOpt.get();
		Optional<CarDetails> carDetailsOpt = carDetailsRepository.findById(carRentalRecord.getCarId());
		// 為防止意外發生 查無資料庫車型時僅記錄 不做處理，讓訂單可順利修改狀態 以便結案
		if (!carDetailsOpt.isPresent()) {
			System.out.println("無此車型，詳細請查閱資料庫 表: CarDetails, 對應ID為: " + carRentalRecord.getCarId());
		}else {
			CarDetails carDetails = carDetailsOpt.get();
			carDetails.setStatus(carReservationStatuEnum.name());
			carDetails.setUpdatedAt(deteTime);
			carDetailsRepository.save(carDetails);
		}
		try {
			carRentalRecordRepository.deleteById(reservationId);
			return new ResponseModel<>(StatusEnum.SUCCESS, "刪除訂單成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModel<>(StatusEnum.FAIL, "刪除訂單失敗");
		}
	}

	// synchronized 同步鎖
	private static synchronized String generateReservationId() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateTimeString = now.format(formatter);

		// 每次生成一個新的ID，計數器遞增
		counter++;

		// 補零（例如 1 會變成 000001）
		String counterString = String.format("%06d", counter);

		// 生成唯一的訂單識別碼
		return dateTimeString + counterString;
	}

	@Transactional(rollbackOn = Exception.class)
	private void save(CarDetails carDetails, CarUserInfo carUserInfo, CarRentalRecord carRentalRecord) {
		carDetailsRepository.save(carDetails);
		carUserInfoRepository.save(carUserInfo);
		carRentalRecordRepository.save(carRentalRecord);
	}

	private static boolean compareRetnalDate(LocalDateTime requestRentalStart, LocalDateTime requestRentalEnd,
			LocalDateTime rentalStart, LocalDateTime rentalEnd) {
		return rentalStart.isBefore(requestRentalEnd) && requestRentalStart.isBefore(rentalEnd);
	}

}
