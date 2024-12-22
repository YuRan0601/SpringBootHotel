package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.rent.controller.utils.BaseValidationUtil;
import com.cloudSerenityHotel.rent.controller.utils.ImageShow;
import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;
import com.cloudSerenityHotel.rent.model.CarModelImage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/insert")
public class InsertCarModel extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ModelDao model = new ModelDao();
	ImageShow imageShow = new ImageShow();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		carInsert(request, response);
	}

	protected void carInsert(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String carModel = BaseValidationUtil.checkValueIfNullFillDefault(request, "carModel");
		String description = BaseValidationUtil.checkValueIfNullFillDefault(request, "description");
		String brand = BaseValidationUtil.checkValueIfNullFillDefault(request, "brand");
		BigDecimal fuelEfficiency = new BigDecimal(
				BaseValidationUtil.checkValueIfNullFillDefault(request, "fuelEfficiency", BigDecimal.class));
		int seatingCapacity = Integer
				.parseInt(BaseValidationUtil.checkValueIfNullFillDefault(request, "seatingCapacity", int.class));
		String carType = BaseValidationUtil.checkValueIfNullFillDefault(request, "carType");
		String carSize = BaseValidationUtil.checkValueIfNullFillDefault(request, "carSize");

		CarModelBean car = new CarModelBean();

		car.setCarModel(carModel);
		car.setDescription(description);
		car.setBrand(brand);
		car.setFuelEfficiency(fuelEfficiency);
		car.setSeatingCapacity(seatingCapacity);
		car.setCarType(carType);
		car.setCarSize(carSize);
		LocalDateTime localDateTime = LocalDateTime.now();
		Timestamp timestamp = Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		car.setCreatedAt(timestamp);
		car.setUpdatedAt(timestamp);

		model.insertModel(car);
		List<CarModelBean> cars = model.selectAll();
		List<CarModelImage> carModelImageList = new ArrayList<CarModelImage>();
		for (CarModelBean carModelBean : cars) {
			CarModelImage carModelImage = new CarModelImage();
			carModelImage.setCarId(carModelBean.getCarId());
			carModelImage.setCarModel(carModelBean.getCarModel());
			carModelImage.setBrand(carModelBean.getBrand());
			carModelImage.setImage(imageShow.showCarPrimary(carModelBean.getCarId(), 1));
			carModelImageList.add(carModelImage);
		}

		request.setAttribute("cars", carModelImageList);

		request.getRequestDispatcher("/static/rent/jsp/getCarModelAll.jsp").forward(request, response);
	}

}
