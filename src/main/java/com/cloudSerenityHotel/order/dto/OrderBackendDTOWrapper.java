package com.cloudSerenityHotel.order.dto;

import java.util.List;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 訂單 DTO 包裝器，用於 XML 轉換
 * 這個類的作用是把多個 OrderBackendDTO 包裝成一個 XML 根節點
 */
@XmlRootElement(name = "orders") // 指定這個類在轉成 XML 時的根節點名稱為 <orders>
public class OrderBackendDTOWrapper {
	
	// 用來存放多個 OrderBackendDTO 的 List
	private List<OrderBackendDTO> orders;
	
	/**
     * Getter 方法
     * @XmlElement(name = "order") 表示在 XML 裡每個元素的標籤名稱為 <order>
     * 這樣轉換出來的 XML 結構會像：
     * <orders>
     *     <order> ... </order>
     *     <order> ... </order>
     * </orders>
     */
    @XmlElement(name = "order")
    public List<OrderBackendDTO> getOrders() {
        return orders;
    }
    
    // Setter 方法，用來設定 orders
    public void setOrders(List<OrderBackendDTO> orders) {
        this.orders = orders;
    }
    
}