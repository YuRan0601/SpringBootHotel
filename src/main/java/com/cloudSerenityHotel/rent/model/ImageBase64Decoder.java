package com.cloudSerenityHotel.rent.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class ImageBase64Decoder {

	public String saveImagePath(String imageUrl) {
		ImageCar imageCar = new ImageCar();
		imageCar.setImageUrl(imageUrl);
		return imageUrl;
	}

	public String updateImage(String imageUrl, String existingImagePath) {
		// 去除 Base64 字串的前綴部分，例如 "data:image/png;base64,"
		if (imageUrl != null && imageUrl.startsWith("data:")) {
			int index = imageUrl.indexOf("base64,");
			if (index != -1) {
				imageUrl = imageUrl.substring(index + 7); // 去除前綴
			}
		}
		// 進行 Base64 解碼
		byte[] imageBytes;
		try {
			imageBytes = Base64.getDecoder().decode(imageUrl);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "錯誤：無法解碼 Base64 字串，請檢查傳入的圖片資料是否有效";
		}
		// 嘗試寫入圖片到指定的路徑
		try (FileOutputStream fos = new FileOutputStream(existingImagePath)) {
			fos.write(imageBytes); // 寫入圖片資料
		} catch (IOException e) {
			e.printStackTrace();
			return "圖片更新失敗，錯誤訊息：" + e.getMessage();
		}
		// 成功則返回存儲的圖片路徑
		return existingImagePath;
	}

	public String convertImageToBase64(String imagePath) throws IOException {
		File imageFile = new File(imagePath);
		FileInputStream fileInputStream = null;
		if (!imageFile.exists()) {
			throw new FileNotFoundException("檔案不存在：" + imagePath);
		}
		try {
			// 將圖片轉成 byte 編碼
			fileInputStream = new FileInputStream(imageFile);
			// byte 陣列，設置為圖片文件的大小
			byte[] imageByte = new byte[(int) imageFile.length()];
			// 讀取圖片字節 imageByte 陣列中
			fileInputStream.read(imageByte);
			// 將讀取的自結數據轉換為 Base64 字符串
			return Base64.getEncoder().encodeToString(imageByte);

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}

}
