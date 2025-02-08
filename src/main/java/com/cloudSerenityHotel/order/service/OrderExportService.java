package com.cloudSerenityHotel.order.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

@Service
public class OrderExportService {
	
	// 根據訂單狀態篩選並匯出資料，並根據格式選擇匯出方式
    public void exportOrdersByStatus(List<OrderBackendDTO> orders, String status, String format, String filePath) throws Exception {
        // 篩選出符合狀態的訂單
        List<OrderBackendDTO> filteredOrders = orders.stream()
                .filter(order -> status.equals(order.getOrderStatus()))  // 根據狀態篩選
                .collect(Collectors.toList());

        // 根據篩選的結果進行匯出
        if (filteredOrders.isEmpty()) {
            System.out.println("沒有符合條件的訂單");
            return;
        }
        
        // 處理路徑，這個方法會處理相對路徑並創建目錄
        String finalFilePath = validateAndPreparePath(filePath);

        // 根據使用者選擇的格式來決定匯出方式
        switch (format.toLowerCase()) {
            case "csv":
                exportOrdersToCSV(filteredOrders, finalFilePath);  // 匯出 CSV
                break;
            case "json":
                exportOrdersToJSON(filteredOrders, finalFilePath);  // 匯出 JSON
                break;
            default:
                throw new IllegalArgumentException("不支援的格式: " + format);  // 如果格式不支援
        }
    }
    
    // 驗證並準備路徑
    private String validateAndPreparePath(String filePath) {
        // 檢查是否包含 drive 路徑，處理相對路徑等情況
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("無效的文件路徑");
        }

        // 如果是相對路徑，拼接到預設路徑
        if (!filePath.contains(":")) {
            filePath = "C:/Users/你的使用者名稱/Desktop/" + filePath; // 默認存到桌面
        }

        // 檢查文件目錄是否存在，如果不存在，則創建
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        return filePath;
    }
	
	// CSV
	public void exportOrdersToCSV(List<OrderBackendDTO> orders, String filePath) throws Exception {
		// 使用 UTF-8 編碼寫入 CSV
	    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
	         CSVWriter csvWriter = new CSVWriter(writer)) {
	        // 寫入標題行
	        csvWriter.writeNext(new String[]{
	                "OrderID", "UserID", "ReceiveName", "Email", "PhoneNumber", "Address",
	                "OrderStatus", "PaymentMethod", "TotalAmount", "DiscountAmount", "FinalAmount",
	                "OrderDate", "UpdatedAt", "ProductName", "Quantity", "UnitPrice", "Discount", "Subtotal"
	        });

	        // 寫入訂單資料
	        for (OrderBackendDTO order : orders) {
	            for (OrderItemBackendDTO item : order.getOrderItemsDtos()) {
	                csvWriter.writeNext(new String[]{
	                        String.valueOf(order.getOrderId()),
	                        String.valueOf(order.getUserId()),
	                        order.getReceiveName(),
	                        order.getEmail(),
	                        order.getPhoneNumber(),
	                        order.getAddress(),
	                        order.getOrderStatus(),
	                        order.getPaymentMethod(),
	                        order.getTotalAmount(),
	                        order.getDiscountAmount(),
	                        order.getFinalAmount(),
	                        order.getOrderDate(),
	                        order.getUpdatedAt(),
	                        item.getProductName(),
	                        String.valueOf(item.getQuantity()),
	                        String.valueOf(item.getUnitPrice()),
	                        String.valueOf(item.getDiscount()),
	                        String.valueOf(item.getSubtotal())
	                });
	            }
	        }
	    }
	}
	
	// JSON
	public void exportOrdersToJSON(List<OrderBackendDTO> orders, String filePath) throws IOException {
	    Gson gson = new Gson();
	    try (FileWriter writer = new FileWriter(filePath)) {
	        gson.toJson(orders, writer); // Gson 會處理整個 List 和其中的 OrderItemBackendDTO
	    }
	}
	
	// XML_試不出來
	/*public void exportOrdersToXML(List<OrderBackendDTO> orders, String filePath) throws JAXBException {
	    // 創建 JAXBContext 並指定需要轉換的類型
	    JAXBContext context = JAXBContext.newInstance(OrderBackendDTO.class, OrderItemBackendDTO.class);
	    
	    // 創建 Marshaller
	    Marshaller marshaller = context.createMarshaller();
	    
	    // 設置 Marshaller 格式化輸出
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    
	    // 使用 marshal 將對象轉換為 XML 並寫入檔案
	    marshaller.marshal(orders, new File(filePath));  // 確保 orders 是 OrderBackendDTO 的集合
	}*/
}