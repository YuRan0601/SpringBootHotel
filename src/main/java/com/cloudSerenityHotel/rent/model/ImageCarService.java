package com.cloudSerenityHotel.rent.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.rent.dao.ImageCarRepository;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

import jakarta.transaction.Transactional;

@Service
public class ImageCarService {

	@Autowired
	private ImageCarRepository imageCarRepository;
	@Autowired
	private ImageBase64Decoder imageBase64Decoder;

	private static final String BASE_UPLOAD_DIR = "D://uploads/image/"; // 定義根目錄

	public ResponseModel findByIdImageCar(Integer id) {
		Optional<List<String>> imageUrlOpt = imageCarRepository.findImageUrlByModelId(id);
		if (!imageUrlOpt.isPresent()) {
			return new ResponseModel<String>(StatusEnum.FAIL, "查無ID");
		}

		List<String> base64ImageList = imageUrlOpt.get().stream().map(imageUrl -> {
			try {
				// 如果圖片路徑存在，將轉換為 Base64 編碼
				return imageBase64Decoder.convertImageToBase64(imageUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());

		if (base64ImageList.isEmpty()) {
			return new ResponseModel<>(StatusEnum.FAIL, "圖片轉換為 Base64 失敗");
		}

		return new ResponseModel<>(StatusEnum.SUCCESS, base64ImageList);
	}

	public ResponseModel findAllImageCar() {
		  List<ImageCar> imageResp = imageCarRepository.findAll();
		    if (!CollectionUtils.isEmpty(imageResp)) {
		        // 遍歷車型列表，將每個車型的圖片轉換為 Base64 並更新物件
		        for (ImageCar imageCar : imageResp) {
		            String imageUrl = imageCar.getImageUrl(); // 獲取圖片的路徑
		            try {
		                String base64Image = imageBase64Decoder.convertImageToBase64(imageUrl);
		                imageCar.setBase64Image(base64Image); // 設置對應的 Base64 圖片字串
		            } catch (IOException e) {
		                e.printStackTrace();
		                imageCar.setBase64Image("圖片轉換 Base64 失敗: " + e.getMessage()); // 錯誤時設置錯誤訊息
		            }
		        }
		        return new ResponseModel<List<ImageCar>>(StatusEnum.SUCCESS, imageResp);
		    }
		    return new ResponseModel<String>(StatusEnum.FAIL, "查無資料");
		}

	public List<String> insertImageCar(int modelId, MultipartFile[] imageFiles) throws IOException {
		List<String> imagePaths = new ArrayList<>();

		// 創建車型資料夾路徑
		String carModelFolderPath = BASE_UPLOAD_DIR + modelId; // 例如 "uploads/image/12345"
		File carModelFolder = new File(carModelFolderPath);

		// 如果資料夾不存在，則創建它
		if (!carModelFolder.exists()) {
			carModelFolder.mkdirs(); // 創建多層資料夾
		}

		// 儲存每一張圖片
		for (MultipartFile image : imageFiles) {
			// 為每個圖片生成一個唯一的文件名
			String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
			String pathStr = carModelFolderPath + "/" + fileName;
			Path path = Paths.get(pathStr); // 生成圖片的完整儲存路徑

			// 儲存圖片文件到伺服器
			Files.copy(image.getInputStream(), path);

			// 儲存圖片的路徑
			imagePaths.add(pathStr);

			// 將圖片路徑與車型 ID 一同儲存到資料庫
			ImageCar imageCar = new ImageCar();
			imageCar.setModelId(modelId); // 設定車型 ID
			imageCar.setImageUrl(pathStr); // 設定圖片路徑

			// 儲存到資料庫
			imageCarRepository.save(imageCar); // 儲存圖片的資料到資料庫
		}

		return imagePaths; // 返回所有圖片的路徑
	}

	
//	public ResponseModel updateImageCar(ImageCar imageCar, String imageUrl) {
//		String existingImagePath = imageCar.getImageUrl();
//		String updatedImagePath = imageBase64Decoder.updateImage(imageUrl, existingImagePath);
//		if (updatedImagePath != null) {
//			imageCar.setImageUrl(updatedImagePath);
//			ImageCar imageResp = imageCarRepository.save(imageCar);
//			if (imageResp != null) {
//				return new ResponseModel<ImageCar>(StatusEnum.SUCCESS, imageResp);
//			}
//		}
//		return new ResponseModel<String>(StatusEnum.FAIL, "失敗");
//	}
	
	@Transactional
	public ResponseModel updateImageCar(Integer imageId, Integer modelId) {
	    // Step 1: 查詢該車型的所有圖片實體
	    List<ImageCar> allImages = imageCarRepository.findByModelId(modelId); // 查詢該車型的所有圖片

	    // 檢查是否找到圖片
	    if (allImages.isEmpty()) {
	        return new ResponseModel<String>(StatusEnum.FAIL, "找不到該車型的圖片");
	    }

	    // Step 2: 將所有圖片的 isMainImage 設為 0
	    for (ImageCar img : allImages) {
//	        img.setIsMainImage(0); // 將所有圖片的 isMainImage 設為 0
	    }

	    // Step 3: 查找要設為主圖的圖片，並將其 isMainImage 設為 1
	    Optional<ImageCar> imageToUpdate = imageCarRepository.findById(imageId); // 根據圖片 ID 查找圖片
	    if (imageToUpdate.isPresent()) {
	        ImageCar image = imageToUpdate.get();
//	        image.setIsMainImage(1); // 設定此圖片為主圖
	        imageCarRepository.save(image); // 保存更新後的圖片資訊
	    } else {
	        return new ResponseModel<String>(StatusEnum.FAIL, "找不到指定圖片");
	    }

	    // Step 4: 保存所有圖片更新的資訊（將其他圖片的 isMainImage 設為 0）
	    imageCarRepository.saveAll(allImages); // 保存所有圖片更新的資訊

	    // 返回成功的響應
	    return new ResponseModel<ImageCar>();
	}
	
	@Transactional
	public void deleteImageCar(ImageCar imageCar) {
		imageCarRepository.delete(imageCar);
	}
}
