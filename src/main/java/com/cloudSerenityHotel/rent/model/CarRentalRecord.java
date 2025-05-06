package com.cloudSerenityHotel.rent.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CarRentalRecords")
public class CarRentalRecord {

	@Id
	private String id;

	@Column(name = "customer_id")
	private int customerId;

	@Column(name = "car_id")
	private String carId;

	@Column(name = "rental_type")
	private String rentalType;

	@Column(name = "rental_size")
	private String rentalSize;

	@Column(name = "rental_status")
	private String rentalStatus;

	@Column(name = "rental_start")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime rentalStart;

	@Column(name = "rental_end")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime rentalEnd;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt;

	private Integer amount;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarRentalRecord other = (CarRentalRecord) obj;
		return Objects.equals(id, other.id);
	}

}
