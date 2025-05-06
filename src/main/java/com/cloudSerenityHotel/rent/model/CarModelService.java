package com.cloudSerenityHotel.rent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.booking.dao.BookingOrderRepository;
import com.cloudSerenityHotel.booking.model.BookingOrder;
import com.cloudSerenityHotel.rent.dao.CarModelRepository;
import com.cloudSerenityHotel.rent.dao.CarUserInfoRepository;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;
import com.cloudSerenityHotel.rent.model.utils.TimeProvider;
import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarModelService {

	private final CarModelRepository carModelRepository;

	private final BookingOrderRepository bookingOrderRepository;

	private final TimeProvider timeProvider;

	private final CarUserInfoRepository carUserInfoRepository;

	public CarModel findById(int id) {
		Optional<CarModel> carModelResp = carModelRepository.findById(id);
		return carModelResp.orElse(null);
	}

	public List<CarModel> findAll() {
		return carModelRepository.findAll();
	}

	public Page<CarModel> findAllByPage(Pageable pageable) {
		return carModelRepository.findAll(pageable);
	}

	public ResponseModel getUserDetailByOrderId(Integer orderId) {
		Optional<BookingOrder> bookingOrderOpt = bookingOrderRepository.findById(orderId);
		if (bookingOrderOpt.isPresent()) {
			User user = bookingOrderOpt.get().getUser();
			Integer userId = user.getUserId();
			String email = user.getEmail();
			String userName = user.getUserName();
			Member member = user.getMember();
			LocalDate birthday = member.getBirthday();
			String gender = member.getGender();
			String personalIdNo = member.getPersonalIdNo();
			String passportNo = member.getPassportNo();

			Optional<CarUserInfo> carUserInfoOpt = carUserInfoRepository.findById(userId);
			CarUserInfo carUserInfo;
			if (carUserInfoOpt.isPresent()) {
				carUserInfo = carUserInfoOpt.get();
			} else {
				carUserInfo = new CarUserInfo();
				carUserInfo.setUserId(userId);
				carUserInfo.setEmail(email);
				carUserInfo.setUserName(userName);
				carUserInfo.setGender(gender);
				carUserInfo.setBirthday(birthday);
				carUserInfo.setPersonalIdNo(personalIdNo);
				carUserInfo.setPassportNo(passportNo);
			}

			return new ResponseModel<>(StatusEnum.SUCCESS, carUserInfo);
		}

		return new ResponseModel<>(StatusEnum.FAIL, new CarUserInfo());
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
	public boolean deleteCarModel(int id) {
		Optional<CarModel> carModel = carModelRepository.findById(id);
		if (carModel.isPresent()) {
			carModelRepository.deleteById(id); // 刪除
			return true; // 成功刪除
		} else {
			return false; // 車型不存在
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
