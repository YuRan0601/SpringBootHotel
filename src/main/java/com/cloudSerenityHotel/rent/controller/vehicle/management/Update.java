package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cloudSerenityHotel.rent.controller.utils.ImageShow;
import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/update")
public class Update extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ModelDao model = new ModelDao();
	ImageShow imageShow = new ImageShow();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int carId = Integer.parseInt(request.getParameter("carId"));
		String description = request.getParameter("description");
		String carSize = request.getParameter("carSize");

		LocalDateTime localDateTime = LocalDateTime.now();
		Timestamp timestamp = Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		model.updateDescriptionAndSizeByCarId(carId, description, carSize, timestamp);

		CarModelBean carModelBean = model.selectById(carId);

		request.setAttribute("carId", carModelBean.getCarId());
		request.setAttribute("carModel", carModelBean.getCarModel());
		request.setAttribute("description", carModelBean.getDescription());
		request.setAttribute("brand", carModelBean.getBrand());
		request.setAttribute("fuelEfficiency", carModelBean.getFuelEfficiency());
		request.setAttribute("seatingCapacity", carModelBean.getSeatingCapacity());
		request.setAttribute("totalVehicles", carModelBean.getTotalVehicles());
		request.setAttribute("availableVehicles", carModelBean.getAvailableVehicles());
		request.setAttribute("carType", carModelBean.getCarType());
		request.setAttribute("carSize", carModelBean.getCarSize());

		Timestamp createdTimestamp = carModelBean.getCreatedAt();
		Timestamp updatedTimestamp = carModelBean.getUpdatedAt();
		String createdAt = DateTimeFormatter.ofPattern(CarModelOne.DATE_TIME_FORMATTER)
				.format(createdTimestamp.toLocalDateTime());
		String updatedAt = DateTimeFormatter.ofPattern(CarModelOne.DATE_TIME_FORMATTER)
				.format(updatedTimestamp.toLocalDateTime());
		request.setAttribute("createdAt", createdAt);
		request.setAttribute("updatedAt", updatedAt);

		List<String> images = imageShow.showimageAll(carId);
		request.setAttribute("images", images);

		request.getRequestDispatcher("/static/rent/jsp/getCarOne.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
