package com.cloudSerenityHotel.booking.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Setter
@Getter
@ToString(exclude = "roomType")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "room_type_img")
public class RoomTypeImg implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "img_id")
	private Integer imgId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id")
	private RoomType roomType;
	
	@Column(name = "img_url")
	private String imgUrl;
	
	@Column(name = "is_Primary")
	private Boolean isPrimary;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp createdDate;
	
	@Column(name = "updated_date", insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd a hh:mm:ss")
	private Timestamp updatedDate;
}
