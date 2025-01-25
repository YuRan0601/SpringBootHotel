package com.cloudSerenityHotel.booking.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.cloudSerenityHotel.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "booking_order")
public class BookingOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;
	
	@Column(name = "check_in_date")
	private Date checkInDate;
	
	@Column(name = "check_out_date")
	private Date checkOutDate;
	
	@Column(name = "total_price")
	private BigDecimal totalPrice;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp createdDate;
	
	@Column(name = "updated_date", insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp updatedDate;
}
