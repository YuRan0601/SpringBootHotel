package com.cloudSerenityHotel.rent.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.rent.model.ImageCar;
import com.cloudSerenityHotel.rent.model.ImageCarService;
import com.cloudSerenityHotel.rent.model.api.ResponseModel;
import com.cloudSerenityHotel.rent.model.api.StatusEnum;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "/ImageCar")
public class ImageCarController {

	@Autowired
	private ImageCarService imageCarService;

	@GetMapping(path = "/queryOne/{modelId}")
	@ResponseBody
	public ResponseEntity<?> imageCarQuery(@PathVariable("modelId") int modelId) {
		ResponseModel resp = imageCarService.findByIdImageCar(modelId);
		return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
	}

	@PostMapping(path = "/addImage")
	@ResponseBody
	public ResponseEntity<?> uploadImages(@RequestParam("carId") int modelId, @RequestParam("images") MultipartFile[] imageUrl) {
	    try {
	        // 呼叫 Service 層處理圖片上傳和儲存
	        List<String> imagePaths = imageCarService.insertImageCar(modelId, imageUrl);

	        // 返回成功回應
	        ResponseModel response = new ResponseModel<>(StatusEnum.SUCCESS, imagePaths);
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        // 出現錯誤時的處理
	        ResponseModel errorResponse = new ResponseModel<>(StatusEnum.FAIL, "圖片上傳失敗");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}

	@PutMapping(path = "/update")
	@ResponseBody
	 public ResponseEntity<?> updateImage(@RequestBody  @RequestParam("imageId") Integer imageId, 
		        @RequestParam("modelId") Integer modelId) {
		  try {
		        // 呼叫服務層進行圖片主圖的更新
		        ResponseModel response = imageCarService.updateImageCar(imageId, modelId);

		        // 返回成功或失敗的結果
		        if (response.getStatus() == StatusEnum.SUCCESS) {
		            return ResponseEntity.ok(response);
		        } else {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		        }

		    } catch (Exception e) {
		        // 處理任何例外情況
		        ResponseModel errorResponse = new ResponseModel<>(StatusEnum.FAIL, "處理圖片主圖時出現錯誤");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		    }
		}

	@DeleteMapping(path = "/delete")
	@ResponseBody
	public void imageCarActionDelete(@RequestBody ImageCar imageCar) {
		imageCarService.deleteImageCar(imageCar);
	}
	
	@GetMapping(path = "/queryAll")
	@ResponseBody
	public  ResponseEntity<?> imageCarAll(){
		ResponseModel resp = imageCarService.findAllImageCar();
		return StatusEnum.SUCCESS.equals(resp.getStatus()) ? ResponseEntity.ok(resp.getData())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.getData());
	}
}
