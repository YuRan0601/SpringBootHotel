package com.cloudSerenityHotel.rent.controller.picture.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.cloudSerenityHotel.rent.controller.utils.ImageShow;
import com.cloudSerenityHotel.rent.dao.ModelDao;
import com.cloudSerenityHotel.rent.model.CarModelBean;
import com.cloudSerenityHotel.rent.model.ImagesBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rent/car-model/insert-image")
public class InsertImage extends HttpServlet {

	private static final long serialVersionUID = 1L;

//	private static final String SAVE_PATH = "C:\\Users\\User\\Desktop\\image\\";

	public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd hh:mm";

	ImageShow imageShow = new ImageShow();
	ModelDao modelDao = new ModelDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ImagesBean imagesBean = new ImagesBean();

		int carId = Integer.parseInt(request.getParameter("carId"));

		for (int i = 1; i <= 5; i++) {
			String imageStr = request.getParameter("images" + i);
			if (imageStr == null) {
				break;
			}
			int isPrimary;
			if (i == 1) {
				isPrimary = 1;
			} else {
				isPrimary = 0;
			}
			int index = imageStr.indexOf(",") + 1;
			String image = imageStr.substring(index);
			byte[] byteArr = Base64.getDecoder().decode(image);

			String uuid = UUID.randomUUID().toString();
			String SAVE_PATH = getServletContext().getRealPath("/static/rent/images/upload/");
			String imageUrl = SAVE_PATH + uuid;
			

			try (OutputStream outputStream = new FileOutputStream(new File(imageUrl))) {
				outputStream.write(byteArr);
			}

			imagesBean.setCarId(carId);
			imagesBean.setIsPrimary(isPrimary);
			imagesBean.setImageUrl(imageUrl);

			imageShow.insert(imagesBean);
		}

		CarModelBean carModelBean = modelDao.updateTime(carId);
		request.setAttribute("carId", carModelBean.getCarId());
		request.setAttribute("carModel", carModelBean.getCarModel());
		request.setAttribute("description", carModelBean.getDescription());
		request.setAttribute("brand", carModelBean.getBrand());
		request.setAttribute("fuelEfficiency", carModelBean.getFuelEfficiency());
		request.setAttribute("seatingCapacity", carModelBean.getSeatingCapacity());
		request.setAttribute("totalVehicles", carModelBean.getTotalVehicles());
		request.setAttribute("availableVehicles", carModelBean.getAvailableVehicles());

		Timestamp createdTimestamp = carModelBean.getCreatedAt();
		Timestamp updatedTimestamp = carModelBean.getUpdatedAt();
		String createdAt = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER).format(createdTimestamp.toLocalDateTime());
		String updatedAt = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER).format(updatedTimestamp.toLocalDateTime());
		request.setAttribute("createdAt", createdAt);
		request.setAttribute("updatedAt", updatedAt);
		request.setAttribute("carType", carModelBean.getCarType());
		request.setAttribute("carSize", carModelBean.getCarSize());

		List<String> images = imageShow.showimageAll(carId);
		request.setAttribute("images", images);

		request.getRequestDispatcher("/static/rent/jsp/getCarOne.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
