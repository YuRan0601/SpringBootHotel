package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;
import java.sql.Timestamp;
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

@WebServlet("/rent/car-model/get-one")
public class CarModelOne extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd hh:mm";

	ModelDao model = new ModelDao();
	ImageShow imageShow = new ImageShow();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int carId = Integer.parseInt(request.getParameter("carId"));
		CarModelBean goCarOne = model.selectById(carId);
		request.setAttribute("carId", goCarOne.getCarId());
		request.setAttribute("carModel", goCarOne.getCarModel());
		request.setAttribute("description", goCarOne.getDescription());
		request.setAttribute("brand", goCarOne.getBrand());
		request.setAttribute("fuelEfficiency", goCarOne.getFuelEfficiency());
		request.setAttribute("seatingCapacity", goCarOne.getSeatingCapacity());
		request.setAttribute("totalVehicles", goCarOne.getTotalVehicles());
		request.setAttribute("availableVehicles", goCarOne.getAvailableVehicles());

		Timestamp createdTimestamp = goCarOne.getCreatedAt();
		Timestamp updatedTimestamp = goCarOne.getUpdatedAt();
		String createdAt = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER).format(createdTimestamp.toLocalDateTime());
		String updatedAt = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER).format(updatedTimestamp.toLocalDateTime());
		request.setAttribute("createdAt", createdAt);
		request.setAttribute("updatedAt", updatedAt);
		request.setAttribute("carType", goCarOne.getCarType());
		request.setAttribute("carSize", goCarOne.getCarSize());
		List<String> images = imageShow.showimageAll(carId);
		request.setAttribute("images", images);
		request.getRequestDispatcher("/static/rent/jsp/getCarOne.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
