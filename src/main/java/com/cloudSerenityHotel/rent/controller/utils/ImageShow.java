package com.cloudSerenityHotel.rent.controller.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.cloudSerenityHotel.rent.dao.ImagesDao;
import com.cloudSerenityHotel.rent.model.ImagesBean;

public class ImageShow {

	private ImagesDao imagesDao;

	public ImageShow() {
		this.imagesDao = new ImagesDao();
	}

	public ImagesBean insert(ImagesBean imageBean) {
		return imagesDao.insert(imageBean);
	}

	public List<String> showimageAll(int carId) {

		List<String> list = new ArrayList<>();

		List<ImagesBean> imageAll = imagesDao.selectAllById(carId);

		for (int i = 0; i < imageAll.size(); i++) {
			ImagesBean imagesBean = imageAll.get(i);
			String imageUrl = imagesBean.getImageUrl();
			File image = new File(imageUrl);
			byte[] imageByte = new byte[(int) image.length()];
			try (FileInputStream fileInputStream = new FileInputStream(image);) {
				fileInputStream.read(imageByte);
				list.add(Base64.getEncoder().encodeToString(imageByte));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public String showCarPrimary(Integer carId, Integer isPrimary) {
		ImagesBean imagesBean = imagesDao.selectCarPrimary(carId, isPrimary);
		if (imagesBean != null) {
			String imageUrl = imagesBean.getImageUrl();
			File image = new File(imageUrl);
			byte[] imageByte = new byte[(int) image.length()];
			try (FileInputStream fileInputStream = new FileInputStream(image);) {
				fileInputStream.read(imageByte);
				return Base64.getEncoder().encodeToString(imageByte);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
}
