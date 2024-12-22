package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudSerenityHotel.rent.controller.utils.ImageShow;
import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/select-brand")
public class SelectBrand extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ModelDao model = new ModelDao();
	ImageShow imageShow = new ImageShow();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String brand = request.getParameter("brand");
		if (brand == null) {
			brand = "";
		}

		List<CarModelBean> cars = model.selectByCarBrand(brand);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (CarModelBean carModelBean : cars) {
			String image = imageShow.showCarPrimary(carModelBean.getCarId(), 1);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("carId", carModelBean.getCarId());
			map.put("image", image);
			map.put("carModel", carModelBean.getCarModel());
			map.put("brand", carModelBean.getBrand());
			result.add(map);
		}

		request.setAttribute("cars", result);
		request.getRequestDispatcher("/static/rent/jsp/getCarModelAll.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
