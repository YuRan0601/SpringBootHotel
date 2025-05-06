package com.cloudSerenityHotel.attraction_facility.facility.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "Facility")  // 確認與資料庫的表格名稱匹配
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 設置自動生成的主鍵
    @Column(name = "facility_id")  // 對應資料表的 facility_id 欄位
    private int facilityId;

    @Column(name = "name", nullable = false)  // 設置名稱欄位為不可空
    private String name;

    @Column(name = "description", nullable = true)  // 設置簡介欄位
    private String description;

    @Column(name = "availability_hours", nullable = true)  // 設置開放時間欄位
    private String availabilityHours;

    @Column(name = "location", nullable = true)  // 設置位置欄位
    private String location;

    @Column(name = "create_at", updatable = false)  // 資料產生日期
    private Timestamp createAt;

    @Column(name = "update_at", nullable = false)  // 資料更新日期
    private Timestamp updateAt;

    // Getters and Setters

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailabilityHours() {
        return availabilityHours;
    }

    public void setAvailabilityHours(String availabilityHours) {
        this.availabilityHours = availabilityHours;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
