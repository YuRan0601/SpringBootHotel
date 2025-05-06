package com.cloudSerenityHotel.booking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Setter
@Getter
@ToString(exclude = "imgs")
@NoArgsConstructor
@Entity
@Table(name = "room_type")
public class RoomType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "type_id")
	private Integer typeId;
	
	@Column(name = "type_name")
	private String typeName;
	
	@Column(name = "type_desc", columnDefinition = "NVARCHAR(MAX)")
	private String typeDesc;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "max_capacity")
	private Integer maxCapacity;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp createdDate;
	
	@Column(name = "updated_date", insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp updatedDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roomType", cascade = {CascadeType.PERSIST})
	private List<RoomTypeImg> imgs = new ArrayList<RoomTypeImg>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roomType", cascade = {CascadeType.PERSIST})
	private List<Room> rooms = new ArrayList<Room>();

	public RoomType(Integer typeId, String typeName, String typeDesc, Integer maxCapacity, Timestamp createdDate,
			Timestamp updatedDate) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
		this.typeDesc = typeDesc;
		this.maxCapacity = maxCapacity;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	
	
}
