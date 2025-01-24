package com.cloudSerenityHotel.attraction_facility.attraction.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    // 預設建構子
    public Picture() {
    }

    // 全參數建構子
    public Picture(Integer referenceId, String referenceType, String imageUrl, LocalDateTime createAt, LocalDateTime updateAt) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    
    private String fileName;
    private String filePath;
    private long size;


    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }


    // Getters and Setters
    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}