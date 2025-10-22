package com.cloudSerenityHotel.order.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTOWrapper;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;

import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

@Service
@Transactional // 自動交易管理員
/**
 * 匯出訂單_商業邏輯
 */
public class OrderExportService {
	
	private final String downloadDir = System.getProperty("user.home") + "/Downloads/cloud_serenity_orders/";

	/**
     * #前端下載 (不落地)
     */
	public byte[] exportOrdersToBytes(String format, List<OrderBackendDTO> orders) throws IOException, JAXBException {
		if (orders == null || orders.isEmpty()) {
			return new byte[0];
		}
		switch (format.toLowerCase()) {
		case "csv":
			return exportCsvToBytes(orders);
		case "json":
			return exportJsonToBytes(orders);
		case "xml":
			return exportXmlToBytes(orders);
		default:
			throw new IllegalArgumentException("不支援的格式: " + format);
		}
	}

	// --- 以下是拆開的私有方法 ---
	private byte[] exportCsvToBytes(List<OrderBackendDTO> orders) throws IOException {
		try (StringWriter sw = new StringWriter(); CSVWriter csvWriter = new CSVWriter(sw)) {
			sw.write('\uFEFF'); // UTF-8 BOM，防 Excel 中文亂碼
			csvWriter.writeNext(new String[] { "OrderID", "UserID", "ReceiveName", "Email", "PhoneNumber", "Address",
					"OrderStatus", "PaymentMethod", "TotalAmount", "FinalAmount", "OrderDate", "UpdatedAt",
					"ProductName", "Quantity", "UnitPrice", "Discount", "Subtotal" });
			for (OrderBackendDTO order : orders) {
				for (OrderItemBackendDTO item : order.getOrderItemsDtos()) {
					csvWriter.writeNext(new String[] { String.valueOf(order.getOrderId()),
							String.valueOf(order.getUserId()), order.getReceiveName(), order.getEmail(),
							order.getPhoneNumber(), order.getAddress(), order.getOrderStatus(),
							order.getPaymentMethod(), order.getTotalAmount(), order.getFinalAmount(),
							order.getOrderDate(), order.getUpdatedAt(), item.getProductName(),
							String.valueOf(item.getQuantity()), String.valueOf(item.getUnitPrice()),
							String.valueOf(item.getDiscount()), String.valueOf(item.getSubtotal()) });
				}
			}
			return sw.toString().getBytes(StandardCharsets.UTF_8);
		}
	}

	private byte[] exportJsonToBytes(List<OrderBackendDTO> orders) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(orders);
		return jsonStr.getBytes(StandardCharsets.UTF_8);
	}

	private byte[] exportXmlToBytes(List<OrderBackendDTO> orders) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(OrderBackendDTOWrapper.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		OrderBackendDTOWrapper wrapper = new OrderBackendDTOWrapper();
		wrapper.setOrders(orders);

		StringWriter xmlWriter = new StringWriter();
		marshaller.marshal(wrapper, xmlWriter);
		return xmlWriter.toString().getBytes(StandardCharsets.UTF_8);
	}
	
	/**
     * #後端備份 (落地)
     * 匯出訂單 (CSV / JSON / XML)，直接接收查詢後的結果
     *
     * @param format   匯出格式：csv / json / xml
     * @param fileName 檔案名稱
     * @param status   訂單狀態，可為 null 或空字串表示全部
	 * @throws JAXBException 
     */
	public String exportOrders(String format, String fileName, List<OrderBackendDTO> orders)
            throws IOException, JAXBException {
        if (orders == null || orders.isEmpty()) {
            return null;
        }
        // 建立資料夾
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = downloadDir + fileName;
        switch (format.toLowerCase()) {
            case "csv":
                exportToCSV(orders, filePath);
                break;
            case "json":
                exportToJSON(orders, filePath);
                break;
            case "xml":
                exportToXML(orders, filePath);
                break;
            default:
                throw new IllegalArgumentException("不支援的格式: " + format);
        }
        System.out.println("匯出完成 → " + filePath);
        return filePath;
    }
	
	// CSV
	private void exportToCSV(List<OrderBackendDTO> orders, String filePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
				CSVWriter csvWriter = new CSVWriter(writer)) {
			writer.write('\uFEFF'); // UTF-8 BOM，防 Excel 中文亂碼
			csvWriter.writeNext(new String[] { "OrderID", "UserID", "ReceiveName", "Email", "PhoneNumber", "Address",
					"OrderStatus", "PaymentMethod", "TotalAmount", "FinalAmount", "OrderDate", "UpdatedAt",
					"ProductName", "Quantity", "UnitPrice", "Discount", "Subtotal" });
			for (OrderBackendDTO order : orders) {
				for (OrderItemBackendDTO item : order.getOrderItemsDtos()) {
					csvWriter.writeNext(new String[] { String.valueOf(order.getOrderId()),
							String.valueOf(order.getUserId()), order.getReceiveName(), order.getEmail(),
							order.getPhoneNumber(), order.getAddress(), order.getOrderStatus(),
							order.getPaymentMethod(), order.getTotalAmount(), order.getFinalAmount(),
							order.getOrderDate(), order.getUpdatedAt(), item.getProductName(),
							String.valueOf(item.getQuantity()), String.valueOf(item.getUnitPrice()),
							String.valueOf(item.getDiscount()), String.valueOf(item.getSubtotal()) });
				}
			}
		}
	}

	// JSON
	private void exportToJSON(List<OrderBackendDTO> orders, String filePath) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(filePath)) {
			gson.toJson(orders, writer);
		}
	}

	// XML
	private void exportToXML(List<OrderBackendDTO> orders, String filePath) throws JAXBException {
		// 1. 建立 JAXBContext
		JAXBContext context = JAXBContext.newInstance(OrderBackendDTOWrapper.class);
		// 2. 建立 Marshaller
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 美化輸出
		// 3. 將 List 包成 Wrapper
		OrderBackendDTOWrapper wrapper = new OrderBackendDTOWrapper();
		wrapper.setOrders(orders);
		// 4. 寫入檔案
		marshaller.marshal(wrapper, new File(filePath));
	}
	
	// #前端下載 (不落地)_擠在一起版
	// 只是 不寫檔案而已??
//		public byte[] exportOrdersToBytes(String format, List<OrderBackendDTO> orders) throws IOException, JAXBException {
//			if (orders == null || orders.isEmpty()) {
//				return new byte[0];
//			}
//			switch (format.toLowerCase()) {
//			case "csv":
//				try (StringWriter sw = new StringWriter(); CSVWriter csvWriter = new CSVWriter(sw)) {
//					sw.write('\uFEFF'); // UTF-8 BOM，防 Excel 中文亂碼
//					csvWriter.writeNext(new String[] { "OrderID", "UserID", "ReceiveName", "Email", "PhoneNumber",
//							"Address", "OrderStatus", "PaymentMethod", "TotalAmount", "FinalAmount", "OrderDate",
//							"UpdatedAt", "ProductName", "Quantity", "UnitPrice", "Discount", "Subtotal" });
//					for (OrderBackendDTO order : orders) {
//						for (OrderItemBackendDTO item : order.getOrderItemsDtos()) {
//							csvWriter.writeNext(new String[] { String.valueOf(order.getOrderId()),
//									String.valueOf(order.getUserId()), order.getReceiveName(), order.getEmail(),
//									order.getPhoneNumber(), order.getAddress(), order.getOrderStatus(),
//									order.getPaymentMethod(), order.getTotalAmount(), order.getFinalAmount(),
//									order.getOrderDate(), order.getUpdatedAt(), item.getProductName(),
//									String.valueOf(item.getQuantity()), String.valueOf(item.getUnitPrice()),
//									String.valueOf(item.getDiscount()), String.valueOf(item.getSubtotal()) });
//						}
//					}
//					return sw.toString().getBytes(StandardCharsets.UTF_8);
//				}
//			case "json":
//				Gson gson = new GsonBuilder().setPrettyPrinting().create();
//				String jsonStr = gson.toJson(orders);
//				return jsonStr.getBytes(StandardCharsets.UTF_8);
//			case "xml":
//				JAXBContext context = JAXBContext.newInstance(OrderBackendDTOWrapper.class);
//				Marshaller marshaller = context.createMarshaller();
//				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//				OrderBackendDTOWrapper wrapper = new OrderBackendDTOWrapper();
//				wrapper.setOrders(orders);
//				StringWriter xmlWriter = new StringWriter();
//				marshaller.marshal(wrapper, xmlWriter);
//				return xmlWriter.toString().getBytes(StandardCharsets.UTF_8);
//			default:
//				throw new IllegalArgumentException("不支援的格式: " + format);
//			}
//		}
}