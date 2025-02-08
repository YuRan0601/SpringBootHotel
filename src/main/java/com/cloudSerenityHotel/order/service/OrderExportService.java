package com.cloudSerenityHotel.order.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

public class OrderExportService {
	
	// CSV
	public void exportOrdersToCSV(List<OrderBackendDTO> orders, String filePath) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(filePath));
        // 寫入標題行
        writer.writeNext(new String[] { "OrderID", "UserID", "ReceiveName", "Email", "PhoneNumber", "Address", "OrderStatus", "PaymentMethod", "TotalAmount", "DiscountAmount", "FinalAmount", "OrderDate", "UpdatedAt", "ProductName", "Quantity", "UnitPrice", "Discount", "Subtotal" });

        // 寫入訂單資料
        for (OrderBackendDTO order : orders) {
            for (OrderItemBackendDTO item : order.getOrderItemsDtos()) {
                writer.writeNext(new String[] {
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

        writer.close();
    }
	
	// JSON
	public void exportOrdersToJSON(List<OrderBackendDTO> orders, String filePath) throws IOException {
	    Gson gson = new Gson();
	    try (FileWriter writer = new FileWriter(filePath)) {
	        gson.toJson(orders, writer); // Gson 會處理整個 List 和其中的 OrderItemBackendDTO
	    }
	}
	
	// XML
	public void exportOrdersToXML(List<OrderBackendDTO> orders, String filePath) throws JAXBException {
	    JAXBContext context = JAXBContext.newInstance(OrderBackendDTO.class, OrderItemBackendDTO.class);  // 設置上下文，包含所有需要序列化的類
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  // 美化格式
	    marshaller.marshal(orders, new File(filePath));  // 將資料寫入檔案
	}
}