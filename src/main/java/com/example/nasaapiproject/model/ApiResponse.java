package com.example.nasaapiproject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "api_response")
@Data
public class ApiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer photoId;
    private Integer sol;
    private String cameraName;
    private String cameraFullName;
    private String imgSrc;
    private Date earthDate;
    private Integer roverId;
    private String roverName;
    private Date landingDate;
    private Date launchDate;
    private String roverStatus;
    private Integer maxSol;
    private Date maxDate;
    private Integer totalPhotos;

    public int getCameraId() {
        return id;
    }

    public void setCameraId(int cameraId) {
        this.id = cameraId;
    }
}