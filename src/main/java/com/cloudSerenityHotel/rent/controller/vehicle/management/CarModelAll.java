package com.cloudSerenityHotel.rent.controller.vehicle.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.rent.controller.utils.ImageShow;
import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;
import com.cloudSerenityHotel.rent.model.CarModelImage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/query-all")
public class CarModelAll extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ModelDao model = new ModelDao();
	ImageShow imageShow = new ImageShow();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
