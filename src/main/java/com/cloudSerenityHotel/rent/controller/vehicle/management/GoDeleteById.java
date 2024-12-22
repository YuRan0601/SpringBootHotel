package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;

import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/go-delete-by-id")
public class GoDeleteById extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ModelDao model = new ModelDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int carId = Integer.parseInt(request.getParameter("carId"));
		CarModelBean car = model.selectById(carId);
		request.setAttribute("carId", car.getCarId());
		request.setAttribute("carModel", car.getCarModel());
		request.setAttribute("brand", car.getBrand());
		request.setAttribute("fuelEfficiency", car.getFuelEfficiency());
		request.setAttribute("seatingCapacity", car.getSeatingCapacity());
		request.setAttribute("totalVehicles", car.getTotalVehicles());
		request.setAttribute("availableVehicles", car.getAvailableVehicles());
		request.setAttribute("carType", car.getCarType());
		request.setAttribute("carSize", car.getCarSize());
		request.getRequestDispatcher("/static/rent/jsp/goDeleteById.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
