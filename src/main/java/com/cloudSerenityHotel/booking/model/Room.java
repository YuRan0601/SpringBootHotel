package com.cloudSerenityHotel.booking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Integer roomId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id")
	private RoomType roomType;
	
	@Column(name = "room_name")
	private String roomName;
	
	@Column(name = "room_description")
	private String roomDescription;
	
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	private Timestamp createdDate;
	
	@Column(name = "updated_date", insertable = false)
	private Timestamp updatedDate;
	
}
